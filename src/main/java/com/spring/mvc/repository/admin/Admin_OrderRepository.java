package com.spring.mvc.repository.admin;

import com.spring.mvc.domain.Order;
import com.spring.mvc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface Admin_OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o.createdAt, COUNT(o) FROM orders o WHERE o.createdAt BETWEEN :startDate AND :endDate GROUP BY o.createdAt ORDER BY o.createdAt")
    List<Object[]> countOrdersByDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    //List<Order> findByUserUsername(String username); // Remove this
    List<Order> findByUser_Id(Long userId); // Add find by ID

    //In OrderRepository
    List<Order> findByUser(User user);
}