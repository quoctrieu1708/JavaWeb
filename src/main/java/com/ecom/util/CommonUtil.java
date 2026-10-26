package com.ecom.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

	private static final String BRAND_NAME = "E-COMMERCE";

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UserService userService;

	@Value("${spring.mail.username:no-reply@ecommerce.local}")
	private String mailFrom;

	/**
	 * Gửi email đặt lại mật khẩu. Giữ đồng bộ để Controller biết kết quả gửi và báo lỗi cho người dùng.
	 */
	public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

		helper.setFrom(mailFrom, BRAND_NAME);
		helper.setTo(reciepentEmail);

		String content = "<p>Xin chào,</p>" + "<p>Bạn đã yêu cầu đặt lại mật khẩu.</p>"
				+ "<p>Nhấn vào liên kết dưới đây để thay đổi mật khẩu:</p>" + "<p><a href=\"" + url
				+ "\">Đổi mật khẩu</a></p>";
		helper.setSubject("Đặt lại mật khẩu - " + BRAND_NAME);
		helper.setText(content, true);
		try {
			mailSender.send(message);
			return true;
		} catch (Exception e) {
			System.err.println("Gửi email đặt lại mật khẩu thất bại (SMTP chưa cấu hình): " + e.getMessage());
			return false;
		}
	}

	public static String generateUrl(HttpServletRequest request) {

		// http://localhost:8080/forgot-password
		String siteUrl = request.getRequestURL().toString();

		return siteUrl.replace(request.getServletPath(), "");
	}

	/**
	 * Gửi MỘT email xác nhận cho toàn bộ đơn hàng (gồm nhiều sản phẩm).
	 * Chạy bất đồng bộ trên thread pool riêng nên không chặn luồng HTTP của khách hàng.
	 */
	@Async
	public void sendOrderConfirmationMail(List<ProductOrder> items, String status) {
		if (items == null || items.isEmpty()) {
			return;
		}
		try {
			ProductOrder first = items.get(0);
			NumberFormat vnd = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"));

			StringBuilder rows = new StringBuilder();
			double subtotal = 0;
			double discount = 0;
			for (ProductOrder item : items) {
				double lineTotal = item.getPrice() * item.getQuantity();
				subtotal += lineTotal;
				if (item.getDiscountAmount() != null) {
					discount += item.getDiscountAmount();
				}
				rows.append("<tr>")
						.append("<td style=\"padding:6px;border:1px solid #ddd\">").append(HtmlUtils.htmlEscape(item.getProduct().getTitle())).append("</td>")
						.append("<td style=\"padding:6px;border:1px solid #ddd;text-align:center\">").append(item.getQuantity()).append("</td>")
						.append("<td style=\"padding:6px;border:1px solid #ddd;text-align:right\">").append(vnd.format(item.getPrice())).append("đ</td>")
						.append("<td style=\"padding:6px;border:1px solid #ddd;text-align:right\">").append(vnd.format(lineTotal)).append("đ</td>")
						.append("</tr>");
			}

			String shortId = first.getOrderId().substring(0, Math.min(8, first.getOrderId().length()));
			StringBuilder msg = new StringBuilder();
			msg.append("<p>Xin chào ").append(HtmlUtils.htmlEscape(first.getOrderAddress().getFullName())).append(",</p>")
					.append("<p>Cảm ơn bạn đã mua sắm tại <b>").append(BRAND_NAME).append("</b>. Đơn hàng <b>#").append(shortId)
					.append("</b> của bạn hiện <b>").append(HtmlUtils.htmlEscape(status)).append("</b>.</p>")
					.append("<table style=\"border-collapse:collapse;width:100%\">")
					.append("<tr style=\"background:#111;color:#D4AF37\"><th style=\"padding:6px\">Sản phẩm</th><th style=\"padding:6px\">SL</th><th style=\"padding:6px\">Đơn giá</th><th style=\"padding:6px\">Thành tiền</th></tr>")
					.append(rows)
					.append("</table>")
					.append("<p>Tạm tính: <b>").append(vnd.format(subtotal)).append("đ</b>");
			if (discount > 0) {
				msg.append("<br>Giảm giá voucher: <b>-").append(vnd.format(discount)).append("đ</b>");
			}
			msg.append("</p><p>Hình thức thanh toán: ").append(HtmlUtils.htmlEscape(String.valueOf(first.getPaymentType()))).append("</p>");

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
			helper.setFrom(mailFrom, BRAND_NAME);
			helper.setTo(first.getOrderAddress().getEmail());
			helper.setSubject("Xác nhận đơn hàng #" + shortId + " - " + BRAND_NAME);
			helper.setText(msg.toString(), true);
			mailSender.send(message);
		} catch (Exception e) {
			System.err.println("Gửi email xác nhận đơn hàng thất bại (SMTP chưa cấu hình): " + e.getMessage());
		}
	}

	/**
	 * Gửi email thông báo thay đổi trạng thái cho một dòng sản phẩm trong đơn hàng.
	 * Chạy bất đồng bộ (@Async).
	 */
	@Async
	public void sendMailForProductOrder(ProductOrder order, String status) {
		if (order == null) {
			return;
		}
		try {
			String msg = "<p>Xin chào [[name]],</p>"
					+ "<p>Cảm ơn bạn. Đơn hàng của bạn hiện <b>[[orderStatus]]</b>.</p>"
					+ "<p><b>Chi tiết sản phẩm:</b></p>"
					+ "<p>Tên : [[productName]]</p>"
					+ "<p>Danh mục : [[category]]</p>"
					+ "<p>Số lượng : [[quantity]]</p>"
					+ "<p>Giá : [[price]]</p>"
					+ "<p>Hình thức thanh toán : [[paymentType]]</p>";

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

			helper.setFrom(mailFrom, BRAND_NAME);
			helper.setTo(order.getOrderAddress().getEmail());

			msg = msg.replace("[[name]]", HtmlUtils.htmlEscape(order.getOrderAddress().getFullName()));
			msg = msg.replace("[[orderStatus]]", HtmlUtils.htmlEscape(String.valueOf(status)));
			msg = msg.replace("[[productName]]", HtmlUtils.htmlEscape(order.getProduct().getTitle()));
			msg = msg.replace("[[category]]", HtmlUtils.htmlEscape(String.valueOf(order.getProduct().getCategory())));
			msg = msg.replace("[[quantity]]", order.getQuantity().toString());
			msg = msg.replace("[[price]]", order.getPrice().toString());
			msg = msg.replace("[[paymentType]]", HtmlUtils.htmlEscape(String.valueOf(order.getPaymentType())));

			helper.setSubject("Cập nhật trạng thái đơn hàng - " + BRAND_NAME);
			helper.setText(msg, true);
			mailSender.send(message);
		} catch (Exception e) {
			System.err.println("Gửi email cập nhật đơn hàng thất bại (SMTP chưa cấu hình): " + e.getMessage());
		}
	}
	
	public UserDtls getLoggedInUserDetails(Principal p) {
		String email = p.getName();
		UserDtls userDtls = userService.getUserByEmail(email);
		return userDtls;
	}
	

}
