---
description: Workflow kiểm thử tự động Nhóm 5 — Trung tâm Thông báo, Hỗ trợ trực tuyến Live Chat 1-on-1 & Thống kê Admin Dashboard
---

# Workflow Kiểm Thử Nhóm 5: Thông Báo, Live Chat 1-on-1 & Quản Trị Admin

Workflow này tự động hóa việc kiểm tra toàn bộ các chức năng thuộc **Nhóm 5**:
1. **PRF-04:** Tạo thông báo mới cho khách hàng, đếm số thông báo chưa đọc (`getUnreadCount`), đánh dấu từng thông báo và đánh dấu tất cả đã đọc (`markAllAsRead`).
2. **CHAT-01 -> CHAT-03:** Lưu tin nhắn hỗ trợ 1-on-1 giữa Khách hàng và Admin, kiểm tra **cô lập bảo mật phòng chat** giữa các khách hàng khác nhau (`customerA` vs `customerB`) và đánh dấu hội thoại đã đọc.
3. **ADM-01:** Kiểm tra toàn bộ truy vấn thống kê doanh thu, tổng đơn hàng, cảnh báo tồn kho thấp, doanh thu theo ngày và Top sản phẩm bán chạy trên Admin Dashboard.

## Các bước thực hiện

// turbo
1. Chạy file test tự động của Nhóm 5 (`ChatNotificationAndAdminWorkflowTest`):
```powershell
.\mvnw.cmd test "-Dtest=ChatNotificationAndAdminWorkflowTest"
```

2. Kiểm tra kết quả đầu ra trên Terminal:
   - Đảm bảo hiển thị `Tests run: 3, Failures: 0, Errors: 0, Skipped: 0` và `BUILD SUCCESS`.
