package com.spring.mvc.repository.admin;

import com.spring.mvc.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface orderItemRepository  extends JpaRepository<OrderItem,Long> {
}
