package com.spring.mvc.controller.admin;

import com.spring.mvc.domain.Order;
import com.spring.mvc.service.admin.Admin_OrderService; // You'll need to create this service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/order")
public class Admin_OrderController {

    @Autowired
    private Admin_OrderService adminOrderService;

    @GetMapping("/list")
    public String listOrders(Model model) {
        List<Order> orders = adminOrderService.findAllOrders();
        model.addAttribute("orders", orders);
        return "admin/view/manager_order";
    }

    @GetMapping("/detail/{id}")
    public String showOrderDetail(@PathVariable("id") Long id, Model model) {
        Order order = adminOrderService.findOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + id));
        model.addAttribute("order", order);
        return "admin/view/order_detail";
    }
}