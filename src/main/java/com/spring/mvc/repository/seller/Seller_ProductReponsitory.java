package com.spring.mvc.repository.seller;

import com.spring.mvc.domain.Product;
import com.spring.mvc.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Seller_ProductReponsitory extends JpaRepository<Product, Long> {
    List<Product> findBySeller(Seller seller); // Lấy danh sách sản phẩm của một Seller

    @Query("SELECT p FROM Product p WHERE (p.name LIKE %:query% OR p.productType.name LIKE %:query%) AND p.seller = :seller")
    List<Product> findByNameContainingOrProductType_NameContainingAndSeller(@Param("query") String query, @Param("seller") Seller seller);

    List<Product> findBySellerAndStatus(Seller seller, String status);

}
