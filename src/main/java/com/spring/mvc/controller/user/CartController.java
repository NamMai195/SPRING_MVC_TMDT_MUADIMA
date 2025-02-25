package com.spring.mvc.controller.user;

import com.spring.mvc.domain.Cart;
import com.spring.mvc.domain.User;
import com.spring.mvc.service.user.CartService;
import com.spring.mvc.service.user.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // Hiển thị giỏ hàng
    @GetMapping
    public String showCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            List<Cart> cartItems = cartService.getCartItemsByUser(user);
            model.addAttribute("cartItems", cartItems);
        }
        return "user/view/cart";
    }

    // Cập nhật số lượng sản phẩm
    @PostMapping("/update")
    public String updateCartItem(@RequestParam("cartId") Long cartId, @RequestParam("quantity") int quantity) {
        cartService.updateCartItem(cartId, quantity);
        return "redirect:/cart";
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @PostMapping("/delete")
    public String deleteCartItem(@RequestParam("cartId") Long cartId) {
        cartService.removeCartItem(cartId);
        return "redirect:/cart";
    }
    // thêm vào giỏ hàng
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để thêm vào giỏ hàng!");
            return "redirect:/login";
        }

        cartService.addToCart(user, productId);
        redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng!");
        return "redirect:/cart";
    }


}
