package com.spring.mvc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Entity(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "voucher_storage_id")
    private VoucherStorage voucherStorage;


    private Date createdAt;
    private double totalAmount; // Tổng giá trị đơn hàng
    private boolean status;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
}
