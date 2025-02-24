package com.spring.mvc.service.admin;

import com.spring.mvc.domain.Seller;
import com.spring.mvc.repository.admin.Admin_SellerRepository; // You'll need to create this repository
import org.apache.el.parser.AstFalse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class Admin_SellerService {

    @Autowired
    private Admin_SellerRepository adminSellerRepository;

    public List<Seller> findAllSellers() {
        return adminSellerRepository.findAll();
    }
//
    public Optional<Seller> findSellerById(Long id) {
        return adminSellerRepository.findById(id);
    }

    public Seller saveSeller(Seller seller) {
        return adminSellerRepository.save(seller);
    }

    public void deleteSeller(Long id) {
        adminSellerRepository.deleteById(id);
    }
    public List<Seller> findPendingSellers() {
        return adminSellerRepository.findByStatusFalse();
    }
    public void approveSeller(Long id) {
        Seller seller = adminSellerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid seller Id:" + id));
        seller.setStatus(true);
        adminSellerRepository.save(seller);
    }
    public List<Seller> findApprovedSellers() {
        return adminSellerRepository.findByStatusTrue(); // Add this method
    }

    public Object countSellers() {
        return this.adminSellerRepository.count();
    }
}