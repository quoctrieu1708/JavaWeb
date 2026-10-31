# Hướng dẫn sử dụng Postman để nạp 100 sản phẩm chia đều cho 8 danh mục

Thư mục này chứa đầy đủ tài nguyên để bạn có thể quản lý, kiểm thử và nạp danh sách **100 sản phẩm** chia đều cho 8 danh mục chuẩn Figma 2.0:
1. **Điện thoại** (13 sản phẩm)
2. **Laptop** (13 sản phẩm)
3. **Màn hình** (12 sản phẩm)
4. **Gia dụng** (13 sản phẩm)
5. **Thời trang** (13 sản phẩm)
6. **Làm đẹp** (12 sản phẩm)
7. **Sách** (12 sản phẩm)
8. **Thực phẩm** (12 sản phẩm)

---

## 1. Các tệp tin trong thư mục
- `Ecom_Product_100_Seed.postman_collection.json`: File Collection chuẩn Postman v2.1 chứa sẵn các request.
- `products_100.json`: File JSON chứa toàn bộ dữ liệu của 100 sản phẩm (có thể dùng làm Data file cho Collection Runner hoặc import vào hệ thống khác).

---

## 2. Cách Import vào Postman
1. Mở ứng dụng **Postman**.
2. Nhấn nút **Import** (góc trên bên trái).
3. Kéo thả file `postman/Ecom_Product_100_Seed.postman_collection.json` vào Postman.
4. Collection **"E-Commerce System - 100 Products Seeder & API"** sẽ xuất hiện trong workspace của bạn với biến môi trường `baseUrl = http://localhost:8080`.

---

## 3. Các cách nạp 100 sản phẩm qua Postman

### Cách 1: Nạp nhanh 1-Click (Khuyên dùng)
- Chọn request: **`1. Nạp nhanh 100 sản phẩm (One-Click Seed)`**
- Phương thức: `POST`
- URL: `http://localhost:8080/api/products/seed-100`
- Bấm **Send**.
- **Kết quả trả về:** Hệ thống sẽ tự động thêm toàn bộ 100 sản phẩm vào database và báo cáo số lượng phân bổ theo từng danh mục. Nếu sản phẩm đã tồn tại, hệ thống sẽ tự động bỏ qua để tránh trùng lặp.

> *Mẹo: Nếu muốn xóa các sản phẩm mẫu cũ và nạp mới hoàn toàn, dùng request **`2. Làm mới & Nạp lại 100 sản phẩm`** (`POST /api/products/seed-100?reset=true`).*

---

### Cách 2: Nạp hàng loạt bằng mảng JSON (Bulk Insert)
- Chọn request: **`5. Thêm hàng loạt từ mảng JSON (Bulk Insert Products)`**
- Phương thức: `POST`
- URL: `http://localhost:8080/api/products/bulk`
- Body (raw JSON): Copy toàn bộ nội dung từ file `postman/products_100.json` và dán vào tab **Body** trong Postman.
- Bấm **Send**.

---

### Cách 3: Chạy Postman Collection Runner lặp qua 100 sản phẩm
Nếu bạn muốn trải nghiệm gọi 100 request HTTP độc lập cho từng sản phẩm:
1. Trong Postman, click chuột phải vào Collection $\rightarrow$ Chọn **Run collection**.
2. Chỉ tích chọn request: **`4. Thêm sản phẩm đơn lẻ (Create Product - Dùng với Collection Runner)`**.
3. Tại mục **Data**, bấm **Select File** và chọn file `postman/products_100.json`.
4. Postman sẽ tự động nhận diện `100 iterations`.
5. Bấm **Run E-Commerce System...** $\rightarrow$ Postman sẽ tự động gửi 100 request liên tiếp để thêm từng sản phẩm vào DB.

---

## 4. Kiểm tra kết quả
- Gửi request **`3. Thống kê số lượng theo danh mục (Products Summary)`** (`GET /api/products/summary`) để xem thống kê sản phẩm trong database.
- Hoặc mở trình duyệt truy cập `http://localhost:8080/` và `http://localhost:8080/products` để xem sản phẩm hiển thị trên giao diện storefront.
