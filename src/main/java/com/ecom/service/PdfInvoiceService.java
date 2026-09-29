package com.ecom.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecom.model.OrderAddress;
import com.ecom.model.ProductOrder;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;

@Service
public class PdfInvoiceService {

	private Font getFont(float size, int style, Color color) {
		try {
			String fontPath = "C:/Windows/Fonts/arial.ttf";
			if (new File(fontPath).exists()) {
				BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				return new Font(bf, size, style, color);
			}
		} catch (Exception e) {
			// Fallback
		}
		return new Font(Font.HELVETICA, size, style, color);
	}

	public byte[] generateInvoicePdf(List<ProductOrder> orders) throws Exception {
		if (orders == null || orders.isEmpty()) {
			throw new IllegalArgumentException("Đơn hàng không tồn tại");
		}

		ProductOrder firstOrder = orders.get(0);
		OrderAddress addr = firstOrder.getOrderAddress();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4, 36, 36, 36, 36);
		PdfWriter.getInstance(document, out);
		document.open();

		DecimalFormat df = new DecimalFormat("#,###");

		// Header
		Font titleFont = getFont(20, Font.BOLD, new Color(30, 60, 114));
		Font subTitleFont = getFont(10, Font.NORMAL, Color.GRAY);
		Font boldFont = getFont(11, Font.BOLD, Color.BLACK);
		Font normalFont = getFont(10, Font.NORMAL, Color.BLACK);
		Font tableHeaderFont = getFont(10, Font.BOLD, Color.WHITE);

		Paragraph storeTitle = new Paragraph("ECOM STORE", titleFont);
		storeTitle.setAlignment(Element.ALIGN_CENTER);
		document.add(storeTitle);

		Paragraph storeSubtitle = new Paragraph("Địa chỉ: Toà nhà Innovation, Cầu Giấy, Hà Nội | Hotline: 0123 456 789\nWebsite: http://localhost:8080", subTitleFont);
		storeSubtitle.setAlignment(Element.ALIGN_CENTER);
		storeSubtitle.setSpacingAfter(15);
		document.add(storeSubtitle);

		// Line separator
		Paragraph invoiceTitle = new Paragraph("HÓA ĐƠN BÁN HÀNG", getFont(16, Font.BOLD, new Color(42, 82, 152)));
		invoiceTitle.setAlignment(Element.ALIGN_CENTER);
		invoiceTitle.setSpacingAfter(15);
		document.add(invoiceTitle);

		// Order & Customer Info Table (2 columns)
		PdfPTable infoTable = new PdfPTable(2);
		infoTable.setWidthPercentage(100);
		infoTable.setSpacingAfter(15);

		// Col 1: Thông tin đơn hàng
		PdfPCell col1 = new PdfPCell();
		col1.setBorder(0);
		col1.addElement(new Paragraph("THÔNG TIN ĐƠN HÀNG", boldFont));
		col1.addElement(new Paragraph("Mã đơn: #" + firstOrder.getOrderId(), normalFont));
		col1.addElement(new Paragraph("Ngày đặt: " + (firstOrder.getOrderDate() != null ? firstOrder.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : ""), normalFont));
		col1.addElement(new Paragraph("Trạng thái: " + firstOrder.getStatus(), normalFont));
		col1.addElement(new Paragraph("Thanh toán: " + firstOrder.getPaymentType(), normalFont));
		infoTable.addCell(col1);

		// Col 2: Thông tin giao hàng
		PdfPCell col2 = new PdfPCell();
		col2.setBorder(0);
		col2.addElement(new Paragraph("THÔNG TIN NGƯỜI NHẬN", boldFont));
		if (addr != null) {
			col2.addElement(new Paragraph("Họ tên: " + (addr.getFullName() != null ? addr.getFullName() : ""), normalFont));
			col2.addElement(new Paragraph("Điện thoại: " + (addr.getPhone() != null ? addr.getPhone() : ""), normalFont));
			col2.addElement(new Paragraph("Email: " + (addr.getEmail() != null ? addr.getEmail() : ""), normalFont));
			col2.addElement(new Paragraph("Địa chỉ: " + (addr.getDetailAddress() != null ? addr.getDetailAddress() : "") + ", " +
					(addr.getWard() != null ? addr.getWard() : "") + ", " +
					(addr.getProvince() != null ? addr.getProvince() : ""), normalFont));
		}
		infoTable.addCell(col2);
		document.add(infoTable);

		// Product List Table
		PdfPTable itemTable = new PdfPTable(5);
		itemTable.setWidthPercentage(100);
		itemTable.setWidths(new float[]{1, 5, 2, 2.5f, 2.5f});
		itemTable.setSpacingAfter(15);

		Color headerBg = new Color(30, 60, 114);

		String[] headers = {"STT", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
		for (String h : headers) {
			PdfPCell headerCell = new PdfPCell(new Phrase(h, tableHeaderFont));
			headerCell.setBackgroundColor(headerBg);
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headerCell.setPadding(6);
			itemTable.addCell(headerCell);
		}

		double subTotal = 0.0;
		double totalDiscount = 0.0;
		int index = 1;

		for (ProductOrder item : orders) {
			double itemPrice = item.getPrice() != null ? item.getPrice() : 0.0;
			int qty = item.getQuantity() != null ? item.getQuantity() : 1;
			double itemTotal = itemPrice * qty;
			subTotal += itemTotal;

			if (item.getDiscountAmount() != null) {
				totalDiscount += item.getDiscountAmount();
			}

			// STT
			PdfPCell c1 = new PdfPCell(new Phrase(String.valueOf(index++), normalFont));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setPadding(5);
			itemTable.addCell(c1);

			// Title
			PdfPCell c2 = new PdfPCell(new Phrase(item.getProduct() != null ? item.getProduct().getTitle() : "Sản phẩm", normalFont));
			c2.setPadding(5);
			itemTable.addCell(c2);

			// Quantity
			PdfPCell c3 = new PdfPCell(new Phrase(String.valueOf(qty), normalFont));
			c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			c3.setPadding(5);
			itemTable.addCell(c3);

			// Price
			PdfPCell c4 = new PdfPCell(new Phrase(df.format(itemPrice) + "đ", normalFont));
			c4.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c4.setPadding(5);
			itemTable.addCell(c4);

			// Total
			PdfPCell c5 = new PdfPCell(new Phrase(df.format(itemTotal) + "đ", normalFont));
			c5.setHorizontalAlignment(Element.ALIGN_RIGHT);
			c5.setPadding(5);
			itemTable.addCell(c5);
		}
		document.add(itemTable);

		// Summary table (Tiền hàng, giảm giá, phí ship, tổng tiền)
		double shippingFee = 25000.0;
		double grandTotal = Math.max(0.0, subTotal - totalDiscount + shippingFee);

		PdfPTable summaryTable = new PdfPTable(2);
		summaryTable.setWidthPercentage(50);
		summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
		summaryTable.setSpacingAfter(20);

		addSummaryRow(summaryTable, "Tổng tiền hàng:", df.format(subTotal) + "đ", normalFont);
		if (totalDiscount > 0) {
			String voucherInfo = firstOrder.getVoucherCode() != null ? " (" + firstOrder.getVoucherCode() + ")" : "";
			addSummaryRow(summaryTable, "Voucher giảm giá" + voucherInfo + ":", "-" + df.format(totalDiscount) + "đ", normalFont);
		}
		addSummaryRow(summaryTable, "Phí vận chuyển:", df.format(shippingFee) + "đ", normalFont);
		addSummaryRow(summaryTable, "TỔNG THANH TOÁN:", df.format(grandTotal) + "đ", getFont(12, Font.BOLD, new Color(231, 76, 60)));

		document.add(summaryTable);

		// Footer note
		Paragraph thanks = new Paragraph("Cảm ơn quý khách đã tin tưởng và mua sắm tại Ecom Store!\nMọi thắc mắc xin vui lòng liên hệ hotline 0123 456 789 để được hỗ trợ.", getFont(10, Font.ITALIC, Color.DARK_GRAY));
		thanks.setAlignment(Element.ALIGN_CENTER);
		document.add(thanks);

		document.close();
		return out.toByteArray();
	}

	private void addSummaryRow(PdfPTable table, String label, String value, Font font) {
		PdfPCell c1 = new PdfPCell(new Phrase(label, font));
		c1.setBorder(0);
		c1.setPadding(3);
		table.addCell(c1);

		PdfPCell c2 = new PdfPCell(new Phrase(value, font));
		c2.setBorder(0);
		c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c2.setPadding(3);
		table.addCell(c2);
	}
}
