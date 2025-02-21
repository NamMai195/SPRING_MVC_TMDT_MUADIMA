package com.spring.mvc.repository.admin;

import com.spring.mvc.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Admin_ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatus(String status);
}