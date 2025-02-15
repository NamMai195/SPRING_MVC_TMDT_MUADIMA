package com.spring.mvc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToOne(mappedBy = "seller")
    private WalletSeller walletSeller;

    @OneToMany(mappedBy = "seller")
    private List<Product> products;

    @OneToMany(mappedBy = "seller")
    private List<Complain> complains;

    @OneToMany(mappedBy = "seller")
    private List<Voucher> vouchers;
}
