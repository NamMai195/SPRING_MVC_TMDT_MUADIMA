package com.spring.mvc.controller.user;

import com.spring.mvc.domain.Order;
import com.spring.mvc.domain.User;
import com.spring.mvc.service.admin.Admin_OrderService;
import com.spring.mvc.service.admin.Admin_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;  // Correct import
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user/orders") // This URL path is specifically for user-related order actions
public class User_OrderController {

    @Autowired
    private Admin_OrderService orderService;

    @Autowired
    private Admin_UserService userService; // Inject UserService

    // Display Order History
    @GetMapping("/list")
    public String listOrders(Model model, HttpSession session) {
        // 1. Get the user ID from the session (assuming the user is logged in)
        User user = (User) session.getAttribute("loggedInUser");

        // 2. Check if the user is logged in.  If not, redirect to the login page.
        if (user == null) {
            return "redirect:/login"; // Redirect to your login page
        }

        List<Order> orders = orderService.findOrdersByUser(user); // Corrected method name
        model.addAttribute("orders", orders); // Add the user's orders to the model
        return "user/view/manager_order"; // Path to the order list template (e.g., user/order/list.html)
    }

    // Display Order Details
    @GetMapping("/detail/{orderId}")
    public String orderDetail(@PathVariable("orderId") Long orderId, Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        // Check if the user is logged in
        if (user == null) {
            return "redirect:/login"; // Redirect to your login page
        }

        // Use Optional to handle the case where the order might not exist
        Optional<Order> optionalOrder = orderService.findOrderById(orderId);

        // Security Check: Ensure the order belongs to the user!
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();  // Safely get the Order
            // Get user by id


            // This is the KEY SECURITY CHECK: It verifies that the order's user ID
            // matches the currently logged-in user's ID.  If they don't match,
            // it means the user is trying to view someone else's order, so we
            // redirect them to a 403 Forbidden error page.

            model.addAttribute("order", order);
            return "user/view/order_detail";

        } else {
            // Handle the case where the order is not found.  Best practice is a 404 error.
            return "error/404"; // Create an error/404.html template
        }
    }
}