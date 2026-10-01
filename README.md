# 👑 E-COMMERCE — Nền Tảng Thương Mại Điện Tử Toàn Diện (Full-Stack Monolith)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen.svg?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17%20LTS-orange.svg?logo=openjdk)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-9.7%20%2F%208.x-blue.svg?logo=mysql)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Frontend-Thymeleaf%20%2B%20Bootstrap%205-005F0F.svg?logo=thymeleaf)](https://www.thymeleaf.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Dự án website thương mại điện tử đơn khối (**Monolithic Full-Stack E-Commerce Platform**) chuyên nghiệp, được xây dựng trên nền tảng **Java 17**, **Spring Boot 4.1.1**, **Spring Security**, **Spring Data JPA**, **Thymeleaf**, **Bootstrap 5** và **MySQL**. 

Hệ thống sở hữu giao diện nhận diện thương hiệu **E-COMMERCE** phong cách **Black & Gold Luxury Corporate**, tích hợp đầy đủ quy trình từ duyệt sản phẩm, tìm kiếm thời gian thực, quản lý giỏ hàng thông minh với ràng buộc kho chặt chẽ, thanh toán COD, xuất hóa đơn điện tử PDF đến trung tâm hỗ trợ trực tuyến **Live Chat 1-on-1 qua WebSocket STOMP**.

---

## 📑 Mục lục

1. [Điểm nổi bật của dự án](#-điểm-nổi-bật-của-dự-án)
2. [Ngăn xếp công nghệ (Tech Stack)](#-ngăn-xếp-công-nghệ-tech-stack)
3. [Kiến trúc & Tính năng chi tiết](#-kiến-trúc--tính-năng-chi-tiết)
   - [Phía Khách hàng (User Portal)](#1-phía-khách-hàng-user-portal)
   - [Phía Quản trị viên (Admin Portal)](#2-phía-quản-trị-viên-admin-portal)
4. [Bảo mật & Tối ưu hóa (Security & Reliability)](#-bảo-mật--tối-ưu-hóa-security--reliability)
5. [Cấu trúc thư mục dự án](#-cấu-trúc-thư-mục-dự-án)
6. [Hướng dẫn cài đặt & Khởi chạy](#-hướng-dẫn-cài-đặt--khởi-chạy)
7. [Tài khoản kiểm thử (Default Accounts)](#-tài-khoản-kiểm-thử-default-accounts)
8. [Tác giả & Đóng góp](#-tác-giả--đóng-góp)

---

## 🌟 Điểm nổi bật của dự án

- **Giao diện Black & Gold Luxury Corporate:** Thiết kế đồ họa hiện đại, sang trọng với các dải màu đen huyền thoại kết hợp vàng kim (`#D4AF37`), tối ưu hóa hiển thị trên mọi kích thước màn hình (Mobile, Tablet, Desktop).
- **Tìm kiếm trực tiếp (Live Search Autocomplete):** Gợi ý tức thì khi gõ từ khóa, giới hạn hiển thị 5 sản phẩm đầu tiên kèm thanh cuộn mềm mượt (`overflow-y: auto`), hình ảnh thu nhỏ, nhãn giảm giá và giá bán định dạng chuẩn VNĐ.
- **Giỏ hàng Thông minh & Chống vượt kho:**
  - Nhập số lượng sản phẩm tùy ý với nút tăng/giảm và ô nhập trực tiếp.
  - Ràng buộc trần kho nghiêm ngặt: Tuyệt đối không cho phép thêm hoặc cập nhật vượt quá số lượng hàng tồn kho thực tế.
  - **Tự động cộng dồn số lượng khi thêm sản phẩm trùng lặp**, dọn dẹp các dòng trùng lặp trong cơ sở dữ liệu và hiển thị trạng thái số lượng đã có trong giỏ ngay tại trang chi tiết.
- **Hỗ trợ khách hàng trực tuyến 1-on-1 (WebSocket STOMP):** Mỗi khách hàng có kênh kết nối riêng biệt với quản trị viên, tin nhắn gửi nhận tức thời không cần tải lại trang, lưu trữ lịch sử hội thoại đầy đủ trong database.
- **Hóa đơn điện tử PDF & Voucher khuyến mãi:** Tải hóa đơn PDF chuẩn doanh nghiệp tự động với bảng chi tiết và mã số đơn hàng, áp dụng voucher giảm giá linh hoạt (theo % hoặc số tiền cố định).

---

## 🚀 Ngăn xếp công nghệ (Tech Stack)

### Backend
- **Core:** Java 17 (LTS), Spring Boot 4.1.1 (Spring Framework 7).
- **Security:** Spring Security (Form-based authentication, Role-based Access Control `ROLE_ADMIN` & `ROLE_USER`, BCrypt Password Hashing, Content Security Policy).
- **Persistence:** Spring Data JPA, Hibernate ORM 7.x, HikariCP Connection Pooling.
- **Database:** MySQL 9.7 / 8.x (chuẩn mã hóa `utf8mb4_unicode_ci`).
- **Realtime:** Spring WebSocket, STOMP Messaging Protocol, SockJS.
- **Reporting & Docs:** iTextPDF 5.5.13.3 (Xuất hóa đơn PDF), SpringDoc OpenAPI / Swagger UI 2.8.5.
- **Mail Service:** Spring Boot Starter Mail (JavaMailSender).

### Frontend
- **Template Engine:** Thymeleaf 3 (Việt hóa toàn diện giao diện).
- **Styling:** Bootstrap 5.3.3, Custom CSS (Black & Gold Theme), FontAwesome 6.5.1, Google Fonts (*Plus Jakarta Sans* & *Be Vietnam Pro*).
- **Client Scripting:** JavaScript (ES6+), jQuery 3.7.1, jQuery Validation.

---

## 💡 Kiến trúc & Tính năng chi tiết

### 1. Phía Khách hàng (User Portal)
- **Trang chủ & Khám phá:** Banner nổi bật, đếm ngược Flash Sale, danh mục ngành hàng công nghệ trực quan, danh sách sản phẩm mới nhất & sản phẩm giảm giá mạnh.
- **Bộ lọc & Phân trang đa tiêu chí:** Lọc sản phẩm theo danh mục, khoảng giá, sắp xếp theo giá tăng/giảm/mới nhất, phân trang động mượt mà.
- **Trang Chi tiết Sản phẩm:**
  - Bộ sưu tập hình ảnh, thông số chi tiết, tình trạng tồn kho trong kho.
  - Nhãn hiển thị số lượng sản phẩm đã có trong giỏ hàng (`Trong giỏ: X | Tồn kho: Y`).
  - Hộp nhập số lượng mua với nút tăng giảm và kiểm tra tồn kho tức thì.
  - Đánh giá sao (1-5★) và bình luận phản hồi thực tế từ người mua.
- **Quản lý Giỏ hàng (Cart Management):**
  - Xem danh sách sản phẩm, đơn giá, thành tiền và tổng giá trị đơn hàng.
  - Tăng/giảm hoặc nhập số lượng trực tiếp trong giỏ hàng (tự động điều chỉnh nếu vượt trần kho).
  - Tự động cộng dồn số lượng khi thêm sản phẩm trùng từ trang chi tiết.
- **Đặt hàng & Mã giảm giá (Checkout & Voucher):**
  - Nhập thông tin giao hàng, số điện thoại, địa chỉ nhận hàng.
  - Nhập mã voucher khuyến mãi và kiểm tra tính hợp lệ tức thì trước khi thanh toán.
  - Xác nhận đơn hàng COD an toàn.
- **Lịch sử Đơn hàng & Tải Hóa đơn:**
  - Theo dõi trạng thái đơn hàng (`Chờ xử lý`, `Đang vận chuyển`, `Đã giao hàng`, `Đã hủy`).
  - Tải file hóa đơn mua hàng PDF trực tiếp từ hệ thống.
- **Trung tâm CSKH Trực tuyến:** Bong bóng chat nổi tại góc màn hình, tự động kết nối với Quản trị viên để giải đáp thắc mắc.

### 2. Phía Quản trị viên (Admin Portal)
- **Tổng quan Dashboard:** Thống kê doanh thu, tổng số đơn đặt hàng, tổng số lượng sản phẩm và người dùng đăng ký.
- **Quản lý Sản phẩm & Danh mục:**
  - Thêm mới sản phẩm, cập nhật giá bán, phần trăm khuyến mãi, số lượng tồn kho và ảnh đại diện.
  - Quản lý danh mục hàng hóa (Active / Inactive).
- **Quản lý Đơn hàng:**
  - Theo dõi toàn bộ đơn hàng trong hệ thống, cập nhật trạng thái đơn và tự động gửi email thông báo cho khách hàng khi trạng thái thay đổi.
  - In và xem chi tiết hóa đơn của từng đơn hàng.
- **Quản lý Khuyến mãi (Voucher):**
  - Tạo mới mã voucher, giới hạn ngày bắt đầu/kết thúc, số tiền giảm hoặc tỷ lệ phần trăm giảm, giá trị đơn hàng tối thiểu.
- **Quản lý Người dùng:** Phân quyền, kiểm duyệt tài khoản, kích hoạt hoặc khóa tài khoản vi phạm.
- **Bàn làm việc CSKH (Live Chat Console):**
  - Giao diện hai cột chuyên nghiệp: Danh sách khách hàng cần hỗ trợ bên trái (kèm số tin nhắn chưa đọc) và khung chat trực tiếp bên phải.
  - Tìm kiếm nhanh khách hàng theo tên hoặc email.
  - Trò chuyện riêng tư 1-on-1 với từng khách hàng thông qua WebSocket STOMP.

---

## 🔒 Bảo mật & Tối ưu hóa (Security & Reliability)

- **Xác thực & Phân quyền:** Phân tách rõ ràng giữa khu vực công khai (`/**`), khu vực người dùng (`/user/**`) và khu vực quản trị (`/admin/**`).
- **Mã hóa mật khẩu:** Sử dụng thuật toán `BCryptPasswordEncoder` tiêu chuẩn công nghiệp.
- **Content Security Policy (CSP):** Cấu hình tiêu chuẩn HTTP Security Header (`default-src`, `script-src`, `style-src`, `connect-src`), cho phép tương thích với WebSocket SockJS, CDN Bootstrap và các thao tác form an toàn.
- **Chống lỗi trùng lặp dữ liệu Giỏ hàng:** Thiết lập cơ chế truy vấn JPA an toàn với danh sách (`findCartsByProductIdAndUserId`), tự động gộp và dọn dẹp các dòng dư thừa trong cơ sở dữ liệu nếu có cạnh tranh phiên làm việc (concurrency).
- **Form Submission Chuẩn tắc:** Sử dụng form submit HTML thuần kết hợp DOM Event Listener cho chức năng Thêm vào giỏ hàng, đảm bảo tính ổn định và miễn nhiễm hoàn toàn với lỗi chặn script của trình duyệt.

---

## 📁 Cấu trúc thư mục dự án

```
Java_Web/
├── src/main/java/com/ecom/
│   ├── config/          # Cấu hình Spring Security, WebSocket STOMP, Upload, MVC
│   ├── controller/      # AdminController, HomeController, UserController, ChatController, ProductApiController
│   ├── model/           # Các thực thể JPA (Product, UserDtls, Cart, ProductOrder, SupportMessage, Voucher, Review...)
│   ├── repository/      # Spring Data JPA Repositories (CartRepository, ProductRepository, UserRepository...)
│   ├── service/         # Các Interface nghiệp vụ
│   │   └── impl/        # Hiện thực dịch vụ (CartServiceImpl, OrderServiceImpl, ProductServiceImpl, PdfInvoiceService...)
│   └── util/            # AppConstant, CommonUtil, OrderStatus, CurrencyFormatter
├── src/main/resources/
│   ├── static/          # Tài nguyên tĩnh: /css/style.css, /js/script.js, thư mục ảnh sản phẩm/danh mục
│   ├── templates/       # Giao diện Thymeleaf HTML
│   │   ├── admin/       # Trang quản trị: index, chat, orders, products, add_product, edit_product, users, category
│   │   ├── user/        # Trang người dùng: cart, order, my_orders, profile, notifications, success
│   │   ├── base.html    # Layout khung chung, thanh điều hướng, chân trang & Live chat widget
│   │   ├── index.html   # Trang chủ cửa hàng
│   │   ├── product.html # Danh sách sản phẩm & bộ lọc
│   │   └── view_product.html # Chi tiết sản phẩm & form đặt hàng
│   ├── application.yml  # Cấu hình DataSource, JPA, Mail, Server port, Upload path
│   ├── messages.properties # Đa ngôn ngữ (mặc định)
│   └── messages_vi.properties # Bản dịch tiếng Việt
├── pom.xml              # Maven dependencies và build plugins
└── README.md            # Tài liệu dự án
```

---

## 🛠️ Hướng dẫn cài đặt & Khởi chạy

### 1. Yêu cầu môi trường
- **Java:** JDK 17 trở lên.
- **Maven:** Phiên bản 3.8+ (hoặc dùng tệp wrapper `mvnw.cmd` / `mvnw`).
- **Cơ sở dữ liệu:** MySQL Server phiên bản 8.0 trở lên hoặc 9.x.

### 2. Cấu hình cơ sở dữ liệu MySQL
Mở MySQL Workbench hoặc terminal MySQL và tạo cơ sở dữ liệu mới:
```sql
CREATE DATABASE ecommerce_db_new CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Kiểm tra và cập nhật tài khoản kết nối trong [`src/main/resources/application.yml`](src/main/resources/application.yml) nếu cần:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_db_new?useUnicode=true&characterEncoding=UTF-8&createDatabaseIfNotExist=true
    username: root
    password: YOUR_MYSQL_PASSWORD
```

### 3. Biên dịch và Khởi chạy

**Trên hệ điều hành Windows:**
```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run
```

**Trên Linux / macOS:**
```bash
./mvnw clean compile
./mvnw spring-boot:run
```

### 4. Truy cập các cổng giao diện
- **Cổng thông tin Khách hàng:** [http://localhost:8080](http://localhost:8080)
- **Trang Đăng nhập:** [http://localhost:8080/signin](http://localhost:8080/signin)
- **Trang Đăng ký:** [http://localhost:8080/register](http://localhost:8080/register)
- **Bảng điều khiển Quản trị viên:** [http://localhost:8080/admin/](http://localhost:8080/admin/)
- **Trung tâm Live Chat Quản trị:** [http://localhost:8080/admin/chat](http://localhost:8080/admin/chat)
- **Tài liệu Swagger API Docs:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🔑 Tài khoản kiểm thử (Default Accounts)

| Vai trò (Role) | Email đăng nhập | Mật khẩu mặc định | Mục đích sử dụng |
|---|---|---|---|
| **Quản trị viên (Admin)** | `admin@gmail.com` | `admin123` | Quản trị sản phẩm, duyệt đơn hàng, chat CSKH |
| **Khách hàng (Customer)** | `test@example.com` | `123456` | Trải nghiệm duyệt mua, giỏ hàng, đặt hàng & chat |

*(Bạn cũng có thể tự do đăng ký tài khoản khách hàng mới bất cứ lúc nào qua trang Đăng ký).*

---

## 📄 Tác giả & Đóng góp

- **Họ và tên:** Quốc Triệu (`quoctrieu1708`)
- **Email:** quoctrieu17082005@gmail.com
- **GitHub:** [@quoctrieu1708](https://github.com/quoctrieu1708)

Mọi đóng góp, báo lỗi (Issues) hoặc đề xuất tính năng mới (Pull Requests) luôn được hoan nghênh nồng nhiệt!
