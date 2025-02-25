package com.spring.mvc.service.seller;

import com.spring.mvc.domain.Order;
import com.spring.mvc.domain.Seller;
import com.spring.mvc.repository.seller.Seller_OrderRepository; // Corrected repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class Seller_OrderService {

    @Autowired
    private Seller_OrderRepository orderRepository; // Corrected repository

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }
    // Find orders by seller
    public List<Order> findOrdersBySeller(Seller seller) {
        return orderRepository.findOrdersBySeller(seller); // Correctly uses the repository method
    }

    // You *might* have a saveOrder method here, but if the main OrderService
    // handles general order creation, you might not need a separate one here.
    // It depends on whether sellers can create orders directly, or if orders
    // are *only* created by customers checking out.
}