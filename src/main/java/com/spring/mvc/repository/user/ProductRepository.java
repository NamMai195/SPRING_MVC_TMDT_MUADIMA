package com.spring.mvc.repository.user;

import com.spring.mvc.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop6ByProductType_IdInAndStatus(List<Integer> productTypeIds, String status);
}
