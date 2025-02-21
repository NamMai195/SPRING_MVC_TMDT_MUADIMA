package com.spring.mvc.repository.admin;

import com.spring.mvc.domain.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Admin_ProductTypeRepository extends JpaRepository<ProductType, Long> {
}