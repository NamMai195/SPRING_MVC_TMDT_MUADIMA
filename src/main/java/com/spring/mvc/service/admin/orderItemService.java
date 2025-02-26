package com.spring.mvc.service.admin;

import com.spring.mvc.domain.OrderItem;
import com.spring.mvc.repository.admin.orderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class orderItemService {
    @Autowired
    private orderItemRepository orderItemRepository; // CORRECT! Inject the repository

    //find all
    public List<OrderItem> findAllOrderItems() {
        return orderItemRepository.findAll();
    }
    //find by id
    public Optional<OrderItem> findOrderItemById(Long id){
        return this.orderItemRepository.findById(id);
    }

    //save
    public void saveOrderItem(OrderItem orderItem) {
        this.orderItemRepository.save(orderItem);
    }

    //delete
    public void deleteOrderItem(Long id){
        this.orderItemRepository.deleteById(id);
    }

    //count
    public Long countOrderItems() {
        return this.orderItemRepository.count();
    }

}
