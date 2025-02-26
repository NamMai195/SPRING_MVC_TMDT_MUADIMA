package com.spring.mvc.controller.seller;

import com.spring.mvc.domain.Order;
import com.spring.mvc.domain.Seller;  // Import Seller
import com.spring.mvc.service.seller.Seller_OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/seller/orders") // Corrected URL prefix
public class Seller_OrderController {

    @Autowired
    private Seller_OrderService orderService;


    // Display Order History for the logged-in seller
    @GetMapping("/list")
    public String listOrders(Model model, HttpSession session) {
        // Get the logged-in seller from the session
        Seller seller = (Seller) session.getAttribute("loggedInSeller"); // Use "loggedInSeller"

        // Check if the seller is logged in
        if (seller == null) {
            return "redirect:/sellerlogin"; // Redirect to seller login, adjust as needed
        }

        // Retrieve orders for the current seller
        List<Order> orders = orderService.findOrdersBySeller(seller);
        model.addAttribute("orders", orders);
        return "seller/view/manager_order"; // Path to seller's order list template.  e.g., /templates/seller/order/list.html
    }


    // Display Order Details
    @GetMapping("/detail/{orderId}")
    public String orderDetail(@PathVariable("orderId") Long orderId, Model model, HttpSession session) {

        Seller seller = (Seller) session.getAttribute("loggedInSeller");
        if (seller == null) {
            return "redirect:/sellerlogin"; // Redirect to seller login
        }

        Optional<Order> optionalOrder = orderService.findOrderById(orderId);

        // Security Check: Ensure the order belongs to the seller
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            // Check if order belongs to seller.
//            if (!order.getOrderItems().get(0).getProduct().getSeller().getId().equals(seller.getId())) {
//                return "error/403";  // Or a custom "access denied" page
//            }
            model.addAttribute("order", order);
            return "seller/view/order_detail"; // Path to your order detail template
        } else {
            // Handle order not found
            return "error/404";  // Or a custom "not found" page
        }
    }
}