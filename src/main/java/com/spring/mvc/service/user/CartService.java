/*
package com.spring.mvc.service.user;

import com.spring.mvc.domain.Cart;
import com.spring.mvc.domain.User;
import com.spring.mvc.repository.user.CartReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartReponsitory cartRepository;

    public List<Cart> getCartItemsByUser(User user) {
        List<Cart> cartItems = cartRepository.findByUser(user);
        for (Cart cart : cartItems) {
            cart.getProduct().setFirstImage(cart.getProduct().getFirstImage()); // Đảm bảo firstImage được gán
        }
        return cartItems;
    }
}
*/
