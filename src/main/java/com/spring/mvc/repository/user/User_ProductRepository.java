package com.spring.mvc.repository.user;

import com.spring.mvc.domain.Product;
import com.spring.mvc.domain.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface User_ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatus(String status);
    List<Product> findByProductTypeIdAndStatus(Long typeId, String status);
}



