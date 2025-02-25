package com.spring.mvc.repository.user;

import com.spring.mvc.domain.Cart;
import com.spring.mvc.domain.Product;
import com.spring.mvc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartReponsitory extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
    Cart findByUserAndProduct(User user, Product product);
}
