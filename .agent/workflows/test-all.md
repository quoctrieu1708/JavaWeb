---
description: Workflow chạy kiểm thử tự động tuần tự từng nhóm chức năng (Nhóm 1 -> Nhóm 5) và tổng hợp kết quả
---

# Workflow Kiểm Thử Tổng Thể Tất Cả Các Nhóm Chức Năng (E-COMMERCE)

Workflow này thực thi tuần tự 5 nhóm kiểm thử tự động để xác nhận toàn bộ hệ thống hoạt động chính xác mà không cần thao tác thủ công trên trình duyệt.

## Các bước thực hiện

// turbo
1. Chạy script kiểm thử tuần tự từng nhóm chức năng (`run-workflow-tests.ps1`):
```powershell
powershell -ExecutionPolicy Bypass -File .\run-workflow-tests.ps1
```
