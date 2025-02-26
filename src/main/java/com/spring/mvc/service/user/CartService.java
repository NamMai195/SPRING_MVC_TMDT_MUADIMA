package com.spring.mvc.service.user; // Correct package

import com.spring.mvc.domain.Cart;
import com.spring.mvc.domain.Product;
import com.spring.mvc.domain.User;
import com.spring.mvc.repository.user.CartReponsitory;
import com.spring.mvc.repository.user.User_ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartReponsitory cartRepository; // Corrected name
    @Autowired
    private User_ProductRepository productRepository;

    public List<Cart> getCartItemsByUser(User user) {
        return cartRepository.findByUser(user);
    }

    @Transactional // Important for update operations
    public void updateCartItem(Long cartId, int quantity) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        optionalCart.ifPresent(cart -> {
            if (quantity > 0) {
                cart.setQuantity(quantity);
                cartRepository.save(cart);
            } else {
                cartRepository.delete(cart); // Delete if quantity is 0 or less
            }
        });
    }

    @Transactional
    public void removeCartItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }
    @Transactional
    public void addToCart(User user, Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            // Handle the case where the product doesn't exist.  Throw an exception, log, etc.
            throw new IllegalArgumentException("Product with ID " + productId + " not found.");
        }
        Product product = productOptional.get();

        Cart existingCartItem = cartRepository.findByUserAndProduct(user, product);
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            cartRepository.save(existingCartItem); // Save the updated item
        } else {
            Cart cartItem = new Cart();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartRepository.save(cartItem);
        }
    }
    // Clear cart after checkout.
    @Transactional
    public  void clearCart(User user){
        List<Cart> carts = cartRepository.findByUser(user);
        cartRepository.deleteAll(carts);
    }
}