package com.spring.mvc.service.admin;

import com.spring.mvc.domain.Order;
import com.spring.mvc.repository.admin.Admin_OrderRepository; // You'll need to create this repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class Admin_OrderService {

    @Autowired
    private Admin_OrderRepository adminOrderRepository;

    public List<Order> findAllOrders() {
        return adminOrderRepository.findAll();
    }

    public Optional<Order> findOrderById(Long id) {
        return adminOrderRepository.findById(id);
    }
}