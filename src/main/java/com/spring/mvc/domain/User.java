package com.spring.mvc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore  // ⚠️ Tránh vòng lặp khi serialize
    private String name;
    private String password;
    private String image;
    private String email;
    private String address;
    private String sdt;
    private boolean status;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    private List<ProductReview> productReviews;

    @OneToMany(mappedBy = "user")
    private List<Cart> carts;

    @OneToMany(mappedBy = "user")
    private List<Complain> complains;

    @OneToMany(mappedBy = "user")
    private List<VoucherStorage> voucherStorages;
}