package com.ecom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.model.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

	Optional<Voucher> findByCode(String code);

	Optional<Voucher> findByCodeIgnoreCaseAndIsActiveTrue(String code);

	List<Voucher> findByIsActiveTrue();

	Boolean existsByCode(String code);
}
