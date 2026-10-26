---
description: Workflow kiểm thử tự động Nhóm 4 — Mã giảm giá Voucher, Đặt hàng nguyên tử (@Transactional), Hoàn kho & Hóa đơn PDF
---

# Workflow Kiểm Thử Nhóm 4: Voucher, Đặt Hàng Nguyên Tử, Hoàn Kho & Hóa Đơn PDF

Workflow này tự động hóa việc kiểm tra toàn bộ các chức năng thuộc **Nhóm 4**:
1. **ORD-02 & ORD-03:** Kiểm tra tính hợp lệ của mã giảm giá Voucher (thời hạn, giá trị đơn tối thiểu `minOrderValue`, tính mức giảm `%`).
2. **ORD-04, HIS-01 & HIS-03:** Kiểm tra đặt hàng nguyên tử (`saveOrder`):
   - Trừ tồn kho chính xác cho tất cả sản phẩm trong giỏ.
   - Làm trống giỏ hàng và gắn chung 1 mã `orderId`.
   - Kết xuất file PDF Hóa đơn bán hàng (`PdfInvoiceService`).
   - Tự động hoàn trả tồn kho (`increaseStock`) khi hủy đơn hàng (`OrderStatus.CANCEL`).
3. **ORD-05:** Kiểm tra chống bán vượt tồn kho (ném `InsufficientStockException`) khi sản phẩm bị mua hết ngay trước thời điểm bấm thanh toán.

## Các bước thực hiện

// turbo
1. Chạy file test tự động của Nhóm 4 (`OrderVoucherAndInvoiceWorkflowTest`):
```powershell
.\mvnw.cmd test "-Dtest=OrderVoucherAndInvoiceWorkflowTest"
```

2. Kiểm tra kết quả đầu ra trên Terminal:
   - Đảm bảo hiển thị `Tests run: 3, Failures: 0, Errors: 0, Skipped: 0` và `BUILD SUCCESS`.
