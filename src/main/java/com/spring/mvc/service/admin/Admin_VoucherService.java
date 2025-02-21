package com.spring.mvc.service.admin;

import com.spring.mvc.domain.Seller;
import com.spring.mvc.domain.Voucher;
import com.spring.mvc.repository.admin.Admin_SellerRepository;
import com.spring.mvc.repository.admin.Admin_VoucherRepository; // You'll need to create this repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class Admin_VoucherService {

    @Autowired
    private Admin_VoucherRepository adminVoucherRepository;

    @Autowired
    private Admin_SellerRepository adminSellerRepository;

    public List<Voucher> findAllVouchers() {
        return adminVoucherRepository.findAll();
    }

    public Optional<Voucher> findVoucherById(Long id) {
        return adminVoucherRepository.findById(id);
    }

    public Voucher saveVoucher(Voucher voucher) {
        return adminVoucherRepository.save(voucher);
    }

    public void deleteVoucher(Long id) {
        adminVoucherRepository.deleteById(id);
    }

    public List<Seller> findAllSellers() {
        return adminSellerRepository.findAll();
    }
    public Seller findSellerById(Long id) {
        return adminSellerRepository.findById(id).get();
    }
    public List<Voucher> findPendingVouchers() {
        return adminVoucherRepository.findByStatusFalse();
    }

    public void approveVoucher(Long id) {
        Voucher voucher = adminVoucherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid voucher ID: " + id));
        voucher.setStatus(true);
        adminVoucherRepository.save(voucher);
    }
}