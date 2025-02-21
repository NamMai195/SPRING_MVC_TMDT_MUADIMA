package com.spring.mvc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String password;
    private String email;
    private String nameShop;
    private String image;
    private String address;
    private String sdt;
    private String cccd;
    private boolean status; // Trạng thái người bán

    @OneToOne(mappedBy = "seller")
    private WalletSeller walletSeller;
    @ToString.Exclude
    @OneToMany(mappedBy = "seller")
    private List<Product> products;

    @OneToMany(mappedBy = "seller")
    private List<Complain> complains;
    @ToString.Exclude
    @OneToMany(mappedBy = "seller")
    private List<Voucher> vouchers;
}

