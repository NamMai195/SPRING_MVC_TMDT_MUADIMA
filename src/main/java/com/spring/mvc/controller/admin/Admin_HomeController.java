package com.spring.mvc.controller.admin;

import com.spring.mvc.domain.Order;
import com.spring.mvc.service.admin.Admin_DashboardService;
import com.spring.mvc.service.admin.Admin_OrderService; // Import the Order Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class Admin_HomeController {

    @Autowired
    private Admin_DashboardService dashboardService;

    @Autowired // Inject the Order Service
    private Admin_OrderService orderService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // ... (existing code for charts) ...
        model.addAttribute("totalUsers", dashboardService.countTotalUsers());
        model.addAttribute("totalSellers", dashboardService.countTotalSellers());
        model.addAttribute("totalRevenue", dashboardService.calculateTotalRevenue());
        model.addAttribute("totalOrders", dashboardService.countTotalOrders());

        // Get order statistics for the last 7 days
        Map<LocalDate, Long> orderCounts = dashboardService.getOrderCountsLast7Days();

        // Prepare data for Chart.js
        List<String> chartLabels = new ArrayList<>();
        List<Long> chartData = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Map.Entry<LocalDate, Long> entry : orderCounts.entrySet()) {
            chartLabels.add(entry.getKey().format(formatter));
            chartData.add(entry.getValue());
        }

        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

//        // Get user statistics
//        Map<String, Long> userStatusCounts = dashboardService.getUserStatusCounts();
//        model.addAttribute("userStatusLabels", new ArrayList<>(userStatusCounts.keySet()));
//        model.addAttribute("userStatusData", new ArrayList<>(userStatusCounts.values()));

        // Get all orders and add them to the model
        List<Order> orders = orderService.findAllOrders(); // Use the Order Service
        model.addAttribute("orders", orders);


        return "admin/view/index";
    }
}