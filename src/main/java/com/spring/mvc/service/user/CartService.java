package com.spring.mvc.service.user;

import com.spring.mvc.domain.Cart;
import com.spring.mvc.domain.Product;
import com.spring.mvc.domain.User;
import com.spring.mvc.repository.user.CartReponsitory;
import com.spring.mvc.repository.user.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartReponsitory cartRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Cart> getCartItemsByUser(User user) {
        List<Cart> cartItems = cartRepository.findByUser(user);
        for (Cart cart : cartItems) {
            cart.getProduct().setFirstImage(cart.getProduct().getFirstImage()); // Đảm bảo firstImage được gán
        }
        return cartItems;
    }
    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public void updateCartItem(Long cartId, int quantity) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            if (quantity > 0) {
                cart.setQuantity(quantity);
                cartRepository.save(cart);
            } else {
                cartRepository.delete(cart); // Nếu số lượng = 0 thì xóa luôn
            }
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeCartItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public void addToCart(User user, Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return;

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Cart existingCartItem = cartRepository.findByUserAndProduct(user, product);
        if (existingCartItem != null) {
            // Nếu có rồi thì tăng số lượng
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
        } else {
            // Nếu chưa có thì tạo mới
            Cart cartItem = new Cart();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartRepository.save(cartItem);
        }
    }
}
