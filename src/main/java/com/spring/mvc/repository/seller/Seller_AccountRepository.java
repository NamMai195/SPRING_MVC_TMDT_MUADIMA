package com.spring.mvc.repository.seller;


import com.spring.mvc.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Seller_AccountRepository extends JpaRepository<Seller,Long> {
    Optional<Seller> findByEmail(String email);
    Optional<Seller> findBySdt(String sdt);
    Optional<Seller> findByCccd(String cccd);
    Optional<Seller> findByNameShop(String nameShop);
}
