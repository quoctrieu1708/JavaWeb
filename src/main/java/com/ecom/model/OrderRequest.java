package com.ecom.model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class OrderRequest {

	private String fullName;
	private String email;
	private String phone;
	private String province;
	private String ward;
	private String detailAddress;
	private String paymentType;
	private String voucherCode;
}
