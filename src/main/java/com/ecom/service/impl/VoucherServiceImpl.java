package com.ecom.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.model.Voucher;
import com.ecom.repository.VoucherRepository;
import com.ecom.service.VoucherService;

@Service
public class VoucherServiceImpl implements VoucherService {

	@Autowired
	private VoucherRepository voucherRepository;

	@Override
	public Voucher saveVoucher(Voucher voucher) {
		if (voucher.getId() == null) {
			voucher.setCode(voucher.getCode().trim().toUpperCase());
			voucher.setUsedCount(0);
		}
		return voucherRepository.save(voucher);
	}

	@Override
	public List<Voucher> getAllVouchers() {
		return voucherRepository.findAll();
	}

	@Override
	public Voucher getVoucherById(Integer id) {
		return voucherRepository.findById(id).orElse(null);
	}

	@Override
	public Boolean deleteVoucher(Integer id) {
		if (voucherRepository.existsById(id)) {
			voucherRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public Voucher validateAndGetVoucher(String code, Double orderTotal) {
		if (code == null || code.trim().isEmpty()) {
			return null;
		}

		Voucher voucher = voucherRepository.findByCodeIgnoreCaseAndIsActiveTrue(code.trim()).orElse(null);
		if (voucher == null) {
			return null;
		}

		LocalDate today = LocalDate.now();
		if (voucher.getStartDate() != null && today.isBefore(voucher.getStartDate())) {
			return null;
		}
		if (voucher.getEndDate() != null && today.isAfter(voucher.getEndDate())) {
			return null;
		}

		if (voucher.getUsageLimit() != null && voucher.getUsedCount() != null &&
				voucher.getUsedCount() >= voucher.getUsageLimit()) {
			return null;
		}

		if (voucher.getMinOrderValue() != null && orderTotal != null &&
				orderTotal < voucher.getMinOrderValue()) {
			return null;
		}

		return voucher;
	}

	@Override
	public Double calculateDiscount(Voucher voucher, Double orderTotal) {
		if (voucher == null || orderTotal == null || orderTotal <= 0) {
			return 0.0;
		}

		Double discount = 0.0;
		if (voucher.getDiscountPercent() != null && voucher.getDiscountPercent() > 0) {
			discount = orderTotal * (voucher.getDiscountPercent() / 100.0);
		} else if (voucher.getDiscountAmount() != null && voucher.getDiscountAmount() > 0) {
			discount = voucher.getDiscountAmount();
		}

		// Không giảm vượt quá giá trị đơn hàng
		return Math.min(discount, orderTotal);
	}

	@Override
	public void incrementUsedCount(String code) {
		if (code != null) {
			voucherRepository.findByCodeIgnoreCaseAndIsActiveTrue(code.trim()).ifPresent(v -> {
				int current = v.getUsedCount() != null ? v.getUsedCount() : 0;
				v.setUsedCount(current + 1);
				voucherRepository.save(v);
			});
		}
	}
}
