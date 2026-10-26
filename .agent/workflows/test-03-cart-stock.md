---
description: Workflow kiểm thử tự động Nhóm 3 — Giỏ hàng thông minh, Cộng dồn sản phẩm trùng & Chặn vượt tồn kho
---

# Workflow Kiểm Thử Nhóm 3: Giỏ Hàng Thông Minh & Ràng Buộc Tồn Kho

Workflow này tự động hóa việc kiểm tra toàn bộ các chức năng thuộc **Nhóm 3**:
1. **CART-01 & CART-02:** Thêm sản phẩm mới vào giỏ hàng và kiểm tra **cộng dồn số lượng** khi thêm sản phẩm đã có trong giỏ (đảm bảo không sinh bản ghi thứ 2 trùng lặp trong bảng `cart`).
2. **CART-03:** Kiểm tra tự động giới hạn số lượng ở mức tồn kho tối đa (`stock cap`) khi người dùng cố tình thêm vượt quá tồn kho hiện có.
3. **CART-04 & CART-05:** Kiểm tra nút tăng/giảm (`+` / `-`), nhập số lượng trực tiếp (`updateQuantityDirect`) và tự động xóa sản phẩm khỏi giỏ khi giảm số lượng từ `1` về `0`.

## Các bước thực hiện

// turbo
1. Chạy file test tự động của Nhóm 3 (`CartAndStockConstraintWorkflowTest`):
```powershell
.\mvnw.cmd test "-Dtest=CartAndStockConstraintWorkflowTest"
```

2. Kiểm tra kết quả đầu ra trên Terminal:
   - Đảm bảo hiển thị `Tests run: 3, Failures: 0, Errors: 0, Skipped: 0` và `BUILD SUCCESS`.
