package com.spring.mvc.repository.seller;

import com.spring.mvc.domain.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Seller_ProductTypeReponsitory extends JpaRepository<ProductType, Long> {
    Optional<ProductType> findByName(String name);
}
