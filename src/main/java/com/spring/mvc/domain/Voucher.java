package com.spring.mvc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double discountAmount;
    private String code;
    private String type;
    private String status;
    private Date startDate;
    private Date endDate;

    @OneToMany(mappedBy = "voucher")
    private List<Order> orders; // Voucher có thể được sử dụng trong nhiều đơn hàng

    @OneToMany(mappedBy = "voucher")
    private List<VoucherStorage> voucherStorages;
}

