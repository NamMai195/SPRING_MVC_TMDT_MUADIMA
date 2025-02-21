package com.spring.mvc.repository.admin;

import com.spring.mvc.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Admin_OrderRepository extends JpaRepository<Order, Long> {
}