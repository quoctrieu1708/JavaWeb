package com.ecom.service;

import java.util.List;

import com.ecom.model.Voucher;

public interface VoucherService {

	Voucher saveVoucher(Voucher voucher);

	List<Voucher> getAllVouchers();

	Voucher getVoucherById(Integer id);

	Boolean deleteVoucher(Integer id);

	Voucher validateAndGetVoucher(String code, Double orderTotal);

	Double calculateDiscount(Voucher voucher, Double orderTotal);

	void incrementUsedCount(String code);
}
