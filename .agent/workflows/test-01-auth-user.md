---
description: Workflow kiểm thử tự động Nhóm 1 — Xác thực, Bảo mật, Khóa tài khoản & Hồ sơ cá nhân
---

# Workflow Kiểm Thử Nhóm 1: Xác Thực, Bảo Mật & Hồ Sơ Người Dùng

Workflow này tự động hóa việc kiểm tra toàn bộ các chức năng thuộc **Nhóm 1** thay cho thao tác nhập form thủ công trên trình duyệt:
1. **AUTH-01 & AUTH-02:** Đăng ký tài khoản mới (hỗ trợ tên Tỉnh/Thành phố, Quận/Huyện tiếng Việt có khoảng trắng), kiểm tra mã hóa mật khẩu `BCrypt` và chặn đăng ký trùng Email.
2. **AUTH-05:** Tự động tăng số lần nhập sai mật khẩu (`failedAttempt`) và khóa tài khoản tạm thời (`accountNonLocked = false`) sau 3 lần thất bại liên tiếp.
3. **AUTH-06:** Khởi tạo `resetToken` khi quên mật khẩu, xác thực người dùng qua token và đặt lại mật khẩu mới.
4. **PRF-01 & ADM-06:** Cập nhật thông tin hồ sơ cá nhân, tạo tài khoản Quản trị viên (`ROLE_ADMIN`) và khóa/mở khóa tài khoản bởi Admin.

## Các bước thực hiện

// turbo
1. Chạy file test tự động của Nhóm 1 (`AuthAndUserProfileWorkflowTest`):
```powershell
.\mvnw.cmd test "-Dtest=AuthAndUserProfileWorkflowTest"
```

2. Kiểm tra kết quả đầu ra trên Terminal:
   - Đảm bảo hiển thị `Tests run: 4, Failures: 0, Errors: 0, Skipped: 0` và `BUILD SUCCESS`.
   - Mọi dữ liệu kiểm thử tạm thời đều tự động rollback sau khi kết thúc nhờ `@Transactional`.
