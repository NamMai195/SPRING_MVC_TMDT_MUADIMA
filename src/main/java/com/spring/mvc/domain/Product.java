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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    private String name;
    private double price;
    private int quantity;
    @Column(columnDefinition = "TEXT")
    private String image; // Lưu danh sách ảnh dưới dạng chuỗi JSON
    private String describe;
    private String status;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product")
    private List<ProductReview> productReviews;

    @OneToMany(mappedBy = "product")
    private List<Cart> carts;
}
