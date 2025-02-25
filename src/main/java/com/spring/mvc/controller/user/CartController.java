package com.spring.mvc.controller.user;

import com.spring.mvc.domain.*;
import com.spring.mvc.service.admin.Admin_OrderService;
import com.spring.mvc.service.admin.Admin_ProductService;
import com.spring.mvc.service.admin.orderItemService;
import com.spring.mvc.service.user.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired private orderItemService orderItemService;

    @Autowired private CartService cartService;

    @Autowired private Admin_ProductService productService; // Use the correct service

    @Autowired private Admin_OrderService orderService; // Inject OrderService

    @GetMapping("")
    public String viewCart(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        List<Cart> cartItems = cartService.getCartItemsByUser(user); // Get cart items
        model.addAttribute("cartItems", cartItems);
        return "user/view/cart"; // Path to your cart.html
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam("productId") Long productId,
            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        // Check if a user is logged in (session-based)
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "You need to login to add to cart.");
            return "redirect:/login"; // Redirect to login page
        }

        // cartService.addToCart(productId, quantity, session); //Use session-based.
        cartService.addToCart(user,productId); // Use database.
        redirectAttributes.addFlashAttribute("message", "Product added to cart!");
        return "redirect:/"; // Redirect back to the product listing or wherever is appropriate.
    }

    @PostMapping("/update")
    public String updateCartItem(
            @RequestParam("cartId") Long cartId,
            @RequestParam("quantity") int quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Or handle as you see fit
        }

        // cartService.updateCartItem(cartId,quantity,session); // Session-based.
        cartService.updateCartItem(cartId, quantity); // Use database
        redirectAttributes.addFlashAttribute("message", "Cart updated!");
        return "redirect:/cart"; // Redirect back to the cart
    }

    @PostMapping("/delete")
    public String deleteCartItem(
            @RequestParam("cartId") Long cartId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Or handle as you see fit
        }
        //cartService.removeCartItem(cartId, session); // Remove the item // Use session-based.
        cartService.removeCartItem(cartId);  // Use database
        redirectAttributes.addFlashAttribute("message", "Product removed from cart!");
        return "redirect:/cart";
    }

    // Checkout process
    @PostMapping("/checkout")
    public String checkout(HttpSession session, RedirectAttributes redirectAttributes) {

        // Check authentication (session-based)
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "You must be logged in to checkout.");
            return "redirect:/login";
        }

        // Get cart items from session (or database)
        // List<CartItem> cartItems = cartService.getCartItems(session); // Use session-based.
        List<Cart> cartItems = cartService.getCartItemsByUser(user); // Use database
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty!");
            return "redirect:/cart"; // Redirect back to the cart if it's empty
        }

        // Create the Order entity
        Order order = new Order();
        order.setUser(user); // Very important! Link the order to the logged-in user
        order.setCreatedAt(LocalDateTime.now());

        // Create OrderItems from CartItems and calculate total
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;
        for (Cart cartItem : cartItems) { // Change CartItem to Cart
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order); // Associate the order item with the order
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice()); // Get the product price.
            orderItems.add(orderItem);
            totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();
            orderItem.setSubtotal(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        }
        // Set OrderItems and total amount to the Order.
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setStatus(false); // Initial status
        // Save the order (and cascaded order items)
        orderService.saveOrder(order);

        for (OrderItem OrderItem : orderItems) { // Change CartItem to Cart
             orderItemService.saveOrderItem(OrderItem);
        }

        // Clear the cart (in the session) after successful order creation
        //cartService.clearCart(session); // Use Session-based.
        cartService.clearCart(user);      // Use database
        redirectAttributes.addFlashAttribute("message", "Order placed successfully!  Order ID: " + order.getId());
        return "redirect:/"; // Redirect to a confirmation page, order history, etc.
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {
        // List<CartItem> cartItems = cartService.getCartItems(session); // Use session-based.
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        List<Cart> cartItems = cartService.getCartItemsByUser(user); //Database.
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect back to the cart if it's empty
        }
        model.addAttribute("cartItems", cartItems);
        return "user/view/checkout"; // Return new checkout.html page.
    }
}