package com.spring.mvc.repository.seller; // Correct package

import com.spring.mvc.domain.Order;
import com.spring.mvc.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Seller_OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM orders o JOIN o.orderItems oi WHERE oi.product.seller = :seller")
    List<Order> findOrdersBySeller(@Param("seller") Seller seller);
}