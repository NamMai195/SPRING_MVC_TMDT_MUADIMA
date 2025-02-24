package com.spring.mvc.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Transient
    private String firstImage; // biến tạm lưu ảnh đầu sp

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product")
    private List<ProductReview> productReviews;

    @OneToMany(mappedBy = "product")
    private List<Cart> carts;

    // Phương thức lấy ảnh đầu tiên từ chuỗi JSON
   /* public String getFirstImage() {
        if (this.firstImage == null) { // Nếu chưa có giá trị
            this.firstImage = extractFirstImage();
        }
        return this.firstImage;
    }

    private String extractFirstImage() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(image);
            if (jsonNode.isArray() && jsonNode.size() > 0) {
                return jsonNode.get(0).asText(); // Lấy ảnh đầu tiên
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "default.png"; // Trả về ảnh mặc định nếu không có ảnh
    }*/
}
