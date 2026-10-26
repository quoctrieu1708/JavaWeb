package com.ecom.exception;

/**
 * Ném ra khi số lượng tồn kho không đủ để hoàn tất giao dịch.
 * Là RuntimeException nên sẽ kích hoạt rollback của @Transactional.
 */
public class InsufficientStockException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InsufficientStockException(String message) {
		super(message);
	}
}
