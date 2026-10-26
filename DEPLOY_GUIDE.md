# HƯỚNG DẪN DEPLOY ỨNG DỤNG E-COMMERCE MIỄN PHÍ 100%

Tài liệu này hướng dẫn chi tiết từng bước deploy ứng dụng **Spring Boot 4.1 + MySQL + Thymeleaf + WebSocket** lên Cloud hoàn toàn miễn phí bằng cách kết hợp:
* **Render.com** (Host ứng dụng Spring Boot qua Docker)
* **Aiven.io** hoặc **TiDB Cloud** (Host cơ sở dữ liệu MySQL Server)

---

## BƯỚC 1: KHỞI TẠO CƠ SỞ DỮ LIỆU MYSQL MIỄN PHÍ (AIVEN HOẶC TIDB)

### Cách A: Dùng Aiven.io (Khuyên dùng - Chuẩn MySQL 8.x/9.x)
1. Truy cập [aiven.io](https://aiven.io/) và đăng ký tài khoản miễn phí.
2. Tại bảng điều khiển Aiven, chọn **Create service**.
3. Chọn service **MySQL**, chọn đám mây (AWS hoặc Google Cloud), chọn khu vực gần Việt Nam (ví dụ: `ap-southeast-1` Singapore).
4. Chọn gói **Free Plan** ($0/tháng).
5. Sau khi service khởi tạo xong (khoảng 2–3 phút), bạn sẽ nhận được thông tin kết nối:
   * **Host:** `mysql-xxxx.aivencloud.com`
   * **Port:** `12345`
   * **User:** `avnadmin`
   * **Password:** `...`
   * **Database:** `defaultdb`
6. Mở **DBeaver** hoặc **MySQL Workbench** trên máy tính:
   * Tạo kết nối mới với các thông số trên.
   * Mở file [`database_init.sql`](./database_init.sql) trong thư mục dự án và chạy (Execute Script) để import toàn bộ dữ liệu mẫu (sản phẩm, tài khoản admin/user, danh mục, voucher).

---

## BƯỚC 2: ĐẨY TOÀN BỘ CODE LÊN GITHUB

Mở PowerShell tại thư mục dự án và chạy các lệnh sau:

```powershell
# 1. Kiểm tra trạng thái
git status

# 2. Thêm tất cả thay đổi (bao gồm Dockerfile, database_init.sql, application.yml)
git add .

# 3. Commit code
git commit -m "Prepare Dockerfile, environment configs and database init for cloud deployment"

# 4. Đẩy lên nhánh chính trên GitHub
git push origin master
```

---

## BƯỚC 3: TẠO VÀ DEPLOY WEB SERVICE TRÊN RENDER.COM

1. Truy cập [render.com](https://render.com/) và đăng nhập bằng tài khoản GitHub của bạn.
2. Ở góc trên bên phải, bấm nút **New +** -> chọn **Web Service**.
3. Chọn kho lưu trữ GitHub của dự án: `Java_Web`.
4. Điền các thông số cấu hình:
   * **Name:** `ecommerce-luxury-shop` *(hoặc tên tùy bạn đặt)*
   * **Region:** `Singapore (Southeast Asia)` *(để tốc độ tải trang nhanh nhất từ Việt Nam)*
   * **Branch:** `master`
   * **Runtime:** `Docker` *(Render sẽ tự động phát hiện file `Dockerfile` đã chuẩn bị sẵn)*
   * **Instance Type:** `Free` *(0.5 CPU, 512 MB RAM)*
5. Kéo xuống phần **Environment Variables** (Biến môi trường) -> Bấm **Add Environment Variable** để thêm 3 biến kết nối MySQL từ Bước 1:

| Key (Tên biến) | Value mẫu | Ghi chú |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://<HOST>:<PORT>/<DATABASE>?useSSL=true&characterEncoding=UTF-8` | Thay `<HOST>`, `<PORT>`, `<DATABASE>` bằng thông tin từ Aiven |
| `SPRING_DATASOURCE_USERNAME` | `avnadmin` | User database |
| `SPRING_DATASOURCE_PASSWORD` | `<Mật_khẩu_Aiven>` | Password database |
| `SPRING_MAIL_USERNAME` | `email_cua_ban@gmail.com` | *(Tùy chọn) Để gửi email xác nhận đơn hàng* |
| `SPRING_MAIL_PASSWORD` | `<App_Password_16_ky_tu>` | *(Tùy chọn) Mật khẩu ứng dụng Gmail* |

6. Bấm nút **Create Web Service**.
7. Render sẽ tự động kéo Docker image, build JAR và deploy. Quá trình mất khoảng 3–5 phút. Khi thấy dòng chữ **`==> Your service is live 🎉`**, bạn có thể truy cập website qua đường link được cấp (ví dụ: `https://ecommerce-luxury-shop.onrender.com`).

---

## BƯỚC 4: BÍ QUYẾT TỐI ƯU GÓI FREE RENDER (TRÁNH BỊ SLEEP)

* Trên gói Free của Render, server sẽ tự động "ngủ đông" (sleep) nếu không có truy cập trong 15 phút.
* **Cách khắc phục miễn phí 100%:**
  1. Đăng ký tài khoản miễn phí tại [cron-job.org](https://cron-job.org/) hoặc [uptimerobot.com](https://uptimerobot.com/).
  2. Tạo 1 tác vụ giám sát (Monitor) gọi vào URL web của bạn: `https://ecommerce-luxury-shop.onrender.com/` mỗi **10 phút một lần**.
  3. Server sẽ luôn giữ trạng thái thức 24/7 và phản hồi tức thì cho người dùng!
