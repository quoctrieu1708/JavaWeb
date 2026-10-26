---
description: Workflow kiểm thử tự động Nhóm 2 — Danh mục, Sản phẩm, Bộ lọc nâng cao, Live Search API & Đánh giá sao
---

# Workflow Kiểm Thử Nhóm 2: Danh Mục, Bộ Lọc, Live Search & Đánh Giá Sao

Workflow này tự động hóa việc kiểm tra toàn bộ các chức năng thuộc **Nhóm 2**:
1. **ADM-02 & ADM-03:** Thêm mới Danh mục, thêm mới Sản phẩm và tự động tính toán giá bán sau chiết khấu (`discountPrice`).
2. **HOME-01 & CAT-02:** Kiểm tra truy vấn tối ưu `getLatestActiveProducts(8)` trên trang chủ và bộ lọc kết hợp Danh mục + Khoảng giá (`minPrice`, `maxPrice`) + Sắp xếp (`price_asc`).
3. **HOME-03:** Kiểm tra REST API gợi ý tìm kiếm trực tiếp `/api/products/search-suggestions`.
4. **PRD-04:** Gửi đánh giá sao (`ReviewService`) từ nhiều người dùng và tự động tính lại điểm trung bình (`rating`) cùng tổng lượt nhận xét (`reviewCount`).

## Các bước thực hiện

// turbo
1. Chạy file test tự động của Nhóm 2 (`CatalogSearchAndReviewWorkflowTest`):
```powershell
.\mvnw.cmd test "-Dtest=CatalogSearchAndReviewWorkflowTest"
```

2. Kiểm tra kết quả đầu ra trên Terminal:
   - Đảm bảo hiển thị `Tests run: 4, Failures: 0, Errors: 0, Skipped: 0` và `BUILD SUCCESS`.
