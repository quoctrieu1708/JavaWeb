# E-COMMERCE — Tài Liệu Hệ Thống Thiết Kế (Design System Specification)

## 1. Nhận Diện Thương Hiệu (Brand Identity)
- **Tên thương hiệu:** `E-COMMERCE` (cố định, viết hoa toàn bộ)
- **Phong cách chủ đạo:** Luxury Corporate Black & Gold — Sang trọng, hiện đại, độ tương phản cao, truyền tải sự uy tín của hệ thống bán lẻ công nghệ & thiết bị chính hãng.
- **Ngôn ngữ giao diện:** 100% Tiếng Việt chuẩn mực.

---

## 2. Bảng Màu (Color Tokens)

| Token CSS | Mã Hex | Vai trò sử dụng |
|---|---|---|
| `--gold-primary` | `#D4AF37` | Màu chủ đạo thương hiệu (Nút CTA chính, viền nhấn, icon vương miện, huy hiệu) |
| `--gold-light` | `#F5D77F` | Trạng thái hover sáng, chữ nhấn trên nền tối |
| `--gold-dark` | `#996515` | Chữ vàng đậm trên nền sáng để đảm bảo độ tương phản WCAG |
| `--bg-dark` | `#111111` | Nền Navbar kính mờ (Glassmorphism), Hero Banner, Footer, Flash Sale |
| `--bg-dark-surface` | `#1A1A1A` | Nền thẻ tối phụ trợ, ô tìm kiếm trên Navbar |
| `--bg-canvas` | `#FAFAFA` | Nền tổng thể toàn trang (Light mode ấm, dịu mắt) |
| `--bg-surface` | `#FFFFFF` | Nền thẻ sản phẩm (Product Card), biểu mẫu, bảng dữ liệu |
| `--text-main` | `#18181B` | Màu chữ nội dung chính |
| `--text-muted` | `#71717A` | Màu chữ mô tả phụ, nhãn thông tin |
| `--price-color` | `#DC2626` | Màu giá bán khuyến mãi nổi bật |
| `--success` | `#10B981` | Trạng thái còn hàng, hoàn thành, thông báo thành công |
| `--warning` | `#F59E0B` | Ngôi sao đánh giá, cảnh báo sắp hết hàng |
| `--danger` | `#EF4444` | Huy hiệu giảm giá, nút xóa/hủy, cảnh báo lỗi |

---

## 3. Hệ Thống Chữ (Typography)
- **Headline Font (`h1` – `h6`, Brand, Banner):** `Plus Jakarta Sans` (Weights: `600`, `700`, `800`, Letter-spacing: `-0.02em`).
- **Body & UI Label Font:** `Be Vietnam Pro` (Weights: `400`, `500`, `600`, tối ưu hóa dấu tiếng Việt).
- **Tỷ lệ kích thước (Type Scale):**
  - `display-lg`: `2.5rem` (`40px`), `font-weight: 800`, `line-height: 1.2`
  - `headline-md`: `1.75rem` (`28px`), `font-weight: 700`, `line-height: 1.3`
  - `title-md`: `1.125rem` (`18px`), `font-weight: 700`, `line-height: 1.4`
  - `body-md`: `0.95rem` (`15.2px`), `font-weight: 400`, `line-height: 1.6`
  - `label-sm`: `0.82rem` (`13px`), `font-weight: 600`, `line-height: 1.4`

---

## 4. Bo Góc, Đổ Bóng & Khoảng Cách (Shape, Elevation & Spacing)
- **Bo góc (Border Radius):**
  - `--radius-sm`: `8px` (Huy hiệu nhỏ, ô số lượng)
  - `--radius-md`: `14px` (Ô nhập liệu, nút bấm tiêu chuẩn, menu xổ xuống)
  - `--radius-lg`: `18px` (Thẻ sản phẩm, thẻ danh mục)
  - `--radius-xl`: `24px` (Banner chính, hộp Flash Sale, khung biểu mẫu)
  - `--radius-pill`: `9999px` (Nút tròn, thanh tìm kiếm, huy hiệu trạng thái)
- **Đổ bóng (Elevation Shadows):**
  - `--shadow-card`: `0 10px 30px -5px rgba(0, 0, 0, 0.05), 0 4px 12px rgba(0, 0, 0, 0.02)`
  - `--shadow-hover`: `0 20px 35px -8px rgba(212, 175, 55, 0.22), 0 8px 16px rgba(0, 0, 0, 0.06)`

---

## 5. Danh Mục 26 Màn Hình Trong Dự Án (Screen Inventory)

### A. Phân hệ Công khai & Xác thực (Public & Auth — 9 màn hình)
1. `base.html` — Khung bố cục chung (Navbar Đen & Vàng có Live Search 5 mục cuộn, Toast, Floating Live Chat 1-on-1, Footer 4 cột).
2. `index.html` — Trang chủ (Hero Slider, Thanh cam kết, Flash Sale đếm ngược, Danh mục nổi bật, Sản phẩm gợi ý).
3. `product.html` — Danh sách & Bộ lọc sản phẩm (Tìm kiếm, lọc danh mục/khoảng giá, sắp xếp, chuyển đổi Grid/List, phân trang).
4. `view_product.html` — Chi tiết sản phẩm (Ảnh lớn, giá giảm, tồn kho, bộ chọn số lượng có trần kho, huy hiệu "Trong giỏ", đánh giá sao & sản phẩm liên quan).
5. `login.html` — Đăng nhập tài khoản.
6. `register.html` — Đăng ký tài khoản mới (kèm tải ảnh đại diện).
7. `forgot_password.html` — Yêu cầu đặt lại mật khẩu qua Email.
8. `reset_password.html` — Đặt lại mật khẩu mới.
9. `message.html` — Trang thông báo trạng thái hệ thống.

### B. Phân hệ Khách hàng (`/user/**` — 7 màn hình)
10. `user/cart.html` — Giỏ hàng (Cập nhật số lượng trực tiếp có ràng buộc kho, tính tạm tính).
11. `user/order.html` — Thanh toán & Đặt hàng (Địa chỉ nhận hàng, áp dụng mã Voucher, tóm tắt chi phí).
12. `user/success.html` — Xác nhận đặt hàng thành công.
13. `user/my_orders.html` — Lịch sử đơn hàng của tôi (Theo dõi trạng thái, hủy đơn hoàn kho, tải hóa đơn PDF).
14. `user/profile.html` — Hồ sơ cá nhân & Đổi mật khẩu.
15. `user/notifications.html` — Trung tâm thông báo ưu đãi & đơn hàng.
16. `user/home.html` — Trang tổng quan khách hàng.

### C. Phân hệ Quản trị viên (`/admin/**` — 10 màn hình)
17. `admin/index.html` — Bảng điều khiển Quản trị (6 thẻ KPI, biểu đồ doanh thu, biểu đồ trạng thái đơn, Top 5 bán chạy, đơn mới nhất).
18. `admin/products.html` — Quản lý danh sách sản phẩm & cảnh báo tồn kho.
19. `admin/add_product.html` — Thêm sản phẩm mới.
20. `admin/edit_product.html` — Chỉnh sửa sản phẩm & thiết lập % giảm giá.
21. `admin/category.html` — Quản lý danh mục sản phẩm.
22. `admin/edit_category.html` — Chỉnh sửa danh mục.
23. `admin/orders.html` — Quản lý & cập nhật trạng thái đơn hàng, xuất hóa đơn PDF.
24. `admin/vouchers.html` — Quản lý mã giảm giá (Voucher).
25. `admin/users.html` — Quản lý tài khoản Khách hàng & Quản trị viên.
26. `admin/add_admin.html` — Thêm Quản trị viên mới.
27. `admin/profile.html` — Hồ sơ Quản trị viên.
28. `admin/chat.html` — Trung tâm CSKH Live Chat 1-on-1 thời gian thực.
