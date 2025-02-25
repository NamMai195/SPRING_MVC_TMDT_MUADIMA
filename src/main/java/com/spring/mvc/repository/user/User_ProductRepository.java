package com.spring.mvc.repository.user;

import com.spring.mvc.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface User_ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop6ByProductTypeIdAndStatus(Long productTypeId, String status);
}



