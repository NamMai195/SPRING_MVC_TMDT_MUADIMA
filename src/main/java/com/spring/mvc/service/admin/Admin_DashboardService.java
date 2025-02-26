// Admin_DashboardService.java
package com.spring.mvc.service.admin;

import com.spring.mvc.repository.admin.Admin_OrderRepository;
import com.spring.mvc.repository.admin.Admin_SellerRepository;
import com.spring.mvc.repository.admin.Admin_UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class Admin_DashboardService {

    @Autowired
    private Admin_UserRepository userRepository; // You might not need this in the dashboard
    @Autowired
    private Admin_SellerRepository sellerRepository; // You might not need this in the dashboard
    @Autowired
    private Admin_OrderRepository orderRepository;


    public long countTotalUsers() {
        return userRepository.count();  // You'll need a UserRepository
    }

    public long countTotalSellers() {
        return sellerRepository.count(); // You'll need a SellerRepository
    }
    public double calculateTotalRevenue() {
        return orderRepository.findAll().stream()
                .mapToDouble(order -> {
                    Double amount = order.getTotalAmount();
                    return (amount != null) ? amount : 0.0;
                })
                .sum();
    }

    public long countTotalOrders() {
        return orderRepository.count();
    }
    public Map<LocalDate, Long> getOrderCountsLast7Days() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(6);
        // Convert LocalDate to LocalDateTime, start and end of day
        LocalDateTime startDateTime = sevenDaysAgo.atStartOfDay(); // Start of day 7 days ago
        LocalDateTime endDateTime = today.atTime(LocalTime.MAX);   // End of today
        List<Object[]> results = orderRepository.countOrdersByDate(startDateTime, endDateTime);

        // ... rest of the method remains largely the same, EXCEPT for the casting
        //     You'll need to handle the LocalDateTime result.  Simplest is:

        Map<LocalDate, Long> orderCounts = new TreeMap<>();

        // Initialize the map with all dates in the range (including those with 0 orders)
        LocalDate currentDate = sevenDaysAgo;
        while (!currentDate.isAfter(today)) {
            orderCounts.put(currentDate, 0L); // Initialize with 0
            currentDate = currentDate.plusDays(1);
        }
        for (Object[] result : results) {
            LocalDateTime dateTime = (LocalDateTime) result[0]; // Cast is safe now
            LocalDate date = dateTime.toLocalDate(); // Extract LocalDate
            Long count = (Long) result[1];
            orderCounts.put(date, count);
        }
        return orderCounts;
    }
}