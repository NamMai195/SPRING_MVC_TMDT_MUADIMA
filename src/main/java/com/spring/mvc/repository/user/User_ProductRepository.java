package com.spring.mvc.repository.user;

import com.spring.mvc.domain.Product;
import com.spring.mvc.domain.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface User_ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatus(String status);
    List<Product> findByProductTypeIdAndStatus(Long typeId, String status);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.status = :status")
    List<Product> findByNameContainingIgnoreCaseAndStatus(@Param("keyword") String keyword, @Param("status") String status);
}



