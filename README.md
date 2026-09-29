# 🛒 MODERN E-COMMERCE PLATFORM (HỆ THỐNG THƯƠNG MẠI ĐIỆN TỬ)

Dự án website thương mại điện tử hiện đại, xây dựng trên nền tảng **Java 17**, **Spring Boot 4.1.1** kết hợp **Thymeleaf**, **Bootstrap 5**, **WebSocket STOMP** và cơ sở dữ liệu **MySQL**. Hệ thống được thiết kế hoàn chỉnh từ trải nghiệm mua sắm của khách hàng đến trung tâm quản trị vận hành đa chức năng.

---

## 🚀 Công nghệ sử dụng (Tech Stack)

- **Backend:**
  - **Java 17** (LTS)
  - **Spring Boot 4.1.1** (Spring Framework 7)
  - **Spring Security 7** (Xác thực phân quyền, BCrypt, cơ chế chống Brute-force & IDOR)
  - **Spring Data JPA & Hibernate**
  - **WebSocket STOMP & SockJS** (Giao tiếp thời gian thực)
  - **iTextPDF 5.5.13.3** (Xuất hóa đơn điện tử PDF)
  - **SpringDoc OpenAPI 2.8.5** (Swagger UI documentation)
- **Database:** **MySQL 9.7 / 8.x** (Hỗ trợ chuẩn UTF-8 `utf8mb4_unicode_ci`)
- **Frontend:**
  - **Thymeleaf Template Engine** (Việt hóa 100% giao diện)
  - **Bootstrap 5.3.3** & **FontAwesome 6.5.1**
  - **jQuery & jQuery Validation**

---

## 🌟 Tính năng chính (Key Features)

### 👤 Phía Khách hàng (User Portal)
- **Duyệt & Tìm kiếm Sản phẩm:** Lọc theo danh mục chuẩn xác, phân trang, tìm kiếm theo từ khóa thời gian thực.
- **Giỏ hàng Thông minh (Shopping Cart):** Cập nhật số lượng động, tự động kiểm tra tồn kho, định dạng tiền tệ chuẩn VNĐ (`CurrencyFormatter`).
- **Hệ thống Mã giảm giá (Vouchers):** Áp dụng mã giảm theo phần trăm hoặc số tiền cố định, kiểm tra điều kiện chi tiêu tối thiểu.
- **Đặt hàng & Quản lý đơn:** Hỗ trợ đặt hàng COD, theo dõi trạng thái đơn hàng trực quan (`Chờ xử lý`, `Đang giao`, `Đã giao`, `Đã hủy`).
- **Xuất Hóa đơn Mua hàng PDF:** Tải hóa đơn PDF chuẩn doanh nghiệp ngay tại trang chi tiết đơn hàng.
- **Đánh giá & Xếp hạng (Product Reviews):** Đánh giá sao (1-5★) và bình luận trải nghiệm sản phẩm.
- **Trung tâm Thông báo (User Notifications):** Cập nhật biến động trạng thái đơn hàng, ưu đãi mới.
- **Live Chat CSKH 1-on-1:** Bong bóng chat trực tuyến, kết nối riêng tư trực tiếp tới Quản trị viên, lưu trữ lịch sử đầy đủ.

### 🛡️ Phía Quản trị viên (Admin Portal)
- **Dashboard Quản trị:** Thống kê tổng quan số lượng sản phẩm, đơn hàng, khách hàng và doanh thu.
- **Quản lý Danh mục & Sản phẩm:** Thêm, sửa, xóa, phân loại danh mục, quản lý tồn kho và tải lên hình ảnh sản phẩm an toàn (`FileStorageService`).
- **Quản lý Đơn hàng:** Xem danh sách, cập nhật trạng thái vận chuyển, in hóa đơn PDF cho từng đơn hàng.
- **Quản lý Voucher:** Tạo mã khuyến mãi mới, thiết lập ngày hết hạn, hạn mức giảm và trạng thái kích hoạt.
- **Quản lý Người dùng:** Xem danh sách khách hàng, kích hoạt hoặc khóa tài khoản.
- **Trung tâm CSKH Trực tuyến (1-on-1 Live Chat Console):**
  - Danh sách khách hàng kèm số tin nhắn chưa đọc, thời gian gửi tin cuối.
  - Bộ lọc tìm kiếm nhanh khách hàng theo tên hoặc email.
  - Khung chat riêng biệt 1-on-1 với từng khách hàng, phản hồi tức thì qua WebSocket.

---

## ⚙️ Yêu cầu hệ thống

- **Java Development Kit (JDK):** Version 17 trở lên.
- **Maven:** Version 3.8 trở lên (hoặc sử dụng wrapper `mvnw` đi kèm).
- **Cơ sở dữ liệu:** MySQL 8.0 trở lên hoặc MySQL 9.x.

---

## 🛠️ Hướng dẫn cài đặt & Khởi chạy

### 1. Clone repository
```bash
git clone https://github.com/quoctrieu1708/JavaWeb.git
cd JavaWeb
```

### 2. Cấu hình Cơ sở dữ liệu MySQL
Tạo cơ sở dữ liệu mới trong MySQL:
```sql
CREATE DATABASE ecommerce_db_new CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Cập nhật thông tin kết nối trong file [`src/main/resources/application.yml`](src/main/resources/application.yml) nếu cần:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_db_new?useUnicode=true&characterEncoding=UTF-8&createDatabaseIfNotExist=true
    username: root
    password: YOUR_MYSQL_PASSWORD
```

### 3. Biên dịch và Chạy ứng dụng

**Trên Windows:**
```powershell
.\mvnw.cmd spring-boot:run
```

**Trên Linux / macOS:**
```bash
./mvnw spring-boot:run
```

### 4. Truy cập ứng dụng
- **Trang chủ Khách hàng:** [http://localhost:8080](http://localhost:8080)
- **Trang Đăng nhập:** [http://localhost:8080/signin](http://localhost:8080/signin)
- **Trang Quản trị:** [http://localhost:8080/admin/](http://localhost:8080/admin/)
- **Trung tâm Live Chat CSKH:** [http://localhost:8080/admin/chat](http://localhost:8080/admin/chat)
- **Tài liệu API Swagger:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🔑 Tài khoản mặc định

| Vai trò | Email | Mật khẩu mặc định | Ghi chú |
|---|---|---|---|
| **Quản trị viên (Admin)** | `admin@gmail.com` | `admin123` | Toàn quyền quản trị hệ thống & CSKH |
| **Khách hàng mẫu (User)** | `test@example.com` | `123456` | Tài khoản trải nghiệm mua sắm |

---

## 📁 Cấu trúc thư mục chính

```
Java_Web/
├── src/main/java/com/ecom/
│   ├── config/          # Cấu hình Spring Security, WebSocket, Upload, MVC
│   ├── controller/      # AdminController, HomeController, UserController, ChatController
│   ├── model/           # JPA Entities (Product, UserDtls, ProductOrder, SupportMessage, Voucher, Review...)
│   ├── repository/      # Spring Data JPA Repositories
│   ├── service/         # Interface Services
│   │   └── impl/        # Implementation Services (Order, Product, SupportMessage, Voucher, Invoice...)
│   └── util/            # AppConstant, CommonUtil, OrderStatus, CurrencyFormatter
├── src/main/resources/
│   ├── static/          # CSS, JS, hình ảnh giao diện
│   ├── templates/       # Thymeleaf HTML Templates
│   │   ├── admin/       # Giao diện quản trị, live chat console, đơn hàng, sản phẩm
│   │   ├── user/        # Giao diện cá nhân, giỏ hàng, thông báo, đơn hàng của tôi
│   │   └── base.html    # Layout tổng thể & floating chat widget
│   └── application.yml  # Cấu hình hệ thống chính
└── pom.xml              # Maven dependencies & build plugins
```

---

## 📄 Bản quyền & Tác giả

Dự án được phát triển và duy trì bởi **Quốc Triệu**. Mọi đóng góp và báo lỗi xin vui lòng mở Issue hoặc Pull Request trên GitHub.
