package com.spring.mvc.repository.admin;

import com.spring.mvc.domain.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Admin_VoucherRepository extends JpaRepository<Voucher, Long> {
    List<Voucher> findByStatusFalse();
}