<#
.SYNOPSIS
    Script chạy kiểm thử tự động từng nhóm chức năng (Workflow 1 -> Workflow 5) cho hệ thống E-COMMERCE.
.DESCRIPTION
    Bạn có thể chạy tất cả các nhóm hoặc truyền tham số -Group (1..5) để test riêng từng nhóm chức năng.
.EXAMPLE
    .\run-workflow-tests.ps1           # Chạy lần lượt cả 5 nhóm chức năng
    .\run-workflow-tests.ps1 -Group 3  # Chỉ chạy Nhóm 3 (Giỏ hàng & Tồn kho)
#>
param(
    [ValidateSet(0, 1, 2, 3, 4, 5)]
    [int]$Group = 0
)

$workflows = @(
    @{ Id = 1; Name = "NHOM 1: Xac thuc, Bao mat, Khoa tai khoan & Ho so nguoi dung"; TestClass = "AuthAndUserProfileWorkflowTest" },
    @{ Id = 2; Name = "NHOM 2: Danh muc, San pham, Bo loc, Live Search API & Danh gia sao"; TestClass = "CatalogSearchAndReviewWorkflowTest" },
    @{ Id = 3; Name = "NHOM 3: Gio hang thong minh, Cong don SP trung & Chan vuot ton kho"; TestClass = "CartAndStockConstraintWorkflowTest" },
    @{ Id = 4; Name = "NHOM 4: Voucher, Dat hang nguyen tu (@Transactional), Hoan kho & Hoa don PDF"; TestClass = "OrderVoucherAndInvoiceWorkflowTest" },
    @{ Id = 5; Name = "NHOM 5: Thong bao, Live Chat 1-on-1 & Thong ke Quan tri Admin"; TestClass = "ChatNotificationAndAdminWorkflowTest" }
)

$selected = if ($Group -eq 0) { $workflows } else { $workflows | Where-Object { $_.Id -eq $Group } }

Write-Host "==========================================================================" -ForegroundColor Yellow
Write-Host "   HE THONG KIEM THU TU DONG THEO NHOM CHUC NANG — E-COMMERCE" -ForegroundColor Yellow
Write-Host "==========================================================================" -ForegroundColor Yellow

$allPassed = $true
foreach ($wf in $selected) {
    Write-Host "`n>>> Dang chay [$($wf.Name)] ($($wf.TestClass))..." -ForegroundColor Cyan
    & .\mvnw.cmd test "-Dtest=$($wf.TestClass)" -q
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[PASS] Hoan thanh kiem thu $($wf.Name) THANH CONG!" -ForegroundColor Green
    } else {
        Write-Host "[FAIL] Phat hien loi tai $($wf.Name)!" -ForegroundColor Red
        $allPassed = $false
        break
    }
}

Write-Host "`n==========================================================================" -ForegroundColor Yellow
if ($allPassed) {
    Write-Host "   KET QUA TONG HOP: TAT CA CAC NHOM CHUC NANG DEU HOAT DONG DUNG (100% PASS)" -ForegroundColor Green
} else {
    Write-Host "   KET QUA TONG HOP: CO LOI XAY RA, VUI LONG KIEM TRA LOG PHIA TREN" -ForegroundColor Red
    exit 1
}
Write-Host "==========================================================================" -ForegroundColor Yellow
