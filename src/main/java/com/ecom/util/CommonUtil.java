package com.ecom.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UserService userService;

	public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("daspabitra55@gmail.com", "Ecom Store");
		helper.setTo(reciepentEmail);

		String content = "<p>Xin chào,</p>" + "<p>Bạn đã yêu cầu đặt lại mật khẩu.</p>"
				+ "<p>Nhấn vào liên kết dưới đây để thay đổi mật khẩu:</p>" + "<p><a href=\"" + url
				+ "\">Đổi mật khẩu</a></p>";
		helper.setSubject("Đặt lại mật khẩu - Ecom Store");
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
	
	public Boolean sendMailForProductOrder(ProductOrder order,String status) throws Exception
	{
		
		String msg="<p>Xin chào [[name]],</p>"
				+ "<p>Cảm ơn bạn. Đơn hàng của bạn hiện <b>[[orderStatus]]</b>.</p>"
				+ "<p><b>Chi tiết sản phẩm:</b></p>"
				+ "<p>Tên : [[productName]]</p>"
				+ "<p>Danh mục : [[category]]</p>"
				+ "<p>Số lượng : [[quantity]]</p>"
				+ "<p>Giá : [[price]]</p>"
				+ "<p>Hình thức thanh toán : [[paymentType]]</p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("daspabitra55@gmail.com", "Ecom Store");
		helper.setTo(order.getOrderAddress().getEmail());

		msg=msg.replace("[[name]]",order.getOrderAddress().getFullName());
		msg=msg.replace("[[orderStatus]]",status);
		msg=msg.replace("[[productName]]", order.getProduct().getTitle());
		msg=msg.replace("[[category]]", order.getProduct().getCategory());
		msg=msg.replace("[[quantity]]", order.getQuantity().toString());
		msg=msg.replace("[[price]]", order.getPrice().toString());
		msg=msg.replace("[[paymentType]]", order.getPaymentType());
		
		helper.setSubject("Cập nhật trạng thái đơn hàng - Ecom Store");
		helper.setText(msg, true);
		try {
			mailSender.send(message);
			return true;
		} catch (Exception e) {
			System.err.println("Gửi email cập nhật đơn hàng thất bại (SMTP chưa cấu hình): " + e.getMessage());
			return false;
		}
	}
	
	public UserDtls getLoggedInUserDetails(Principal p) {
		String email = p.getName();
		UserDtls userDtls = userService.getUserByEmail(email);
		return userDtls;
	}
	

}
