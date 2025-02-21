package com.spring.mvc.repository.admin;

import com.spring.mvc.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Admin_SellerRepository extends JpaRepository<Seller, Long> {
    List<Seller> findByStatusFalse();

    List<Seller> findByStatusTrue();
}