/*
package com.spring.mvc.controller.user;

import com.spring.mvc.domain.Cart;
import com.spring.mvc.domain.User;
import com.spring.mvc.service.user.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            List<Cart> cartItems = cartService.getCartItemsByUser(user);

            // Kiểm tra dữ liệu ảnh trước khi render view
            for (Cart cart : cartItems) {
                System.out.println("Product: " + cart.getProduct().getName() +
                        " | First Image: " + cart.getProduct().getFirstImage());
            }

            model.addAttribute("cartItems", cartItems);
        }
        return "/client/view/index";
    }
}
*/
