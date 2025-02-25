package com.spring.mvc.service.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mvc.domain.Product;
import com.spring.mvc.repository.user.User_ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private User_ProductRepository productRepository;

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    public List<Product> getProductsByType(Long typeId) {
        List<Product> products = productRepository.findByProductTypeIdAndStatus(typeId, "Hoạt động");
        setFirstImageForProducts(products);
        return products;
    }
    public List<Product> getActiveProducts() {
        List<Product> products = productRepository.findByStatus("Hoạt động");
        setFirstImageForProducts(products);
        return products;
    }

    private void setFirstImageForProducts(List<Product> products) {
        for (Product product : products) {
            product.setImage(getFirstImageFromJson(product.getImage()));
        }
    }

    private String getFirstImageFromJson(String imageJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(imageJson);
            if (jsonNode.isArray() && jsonNode.size() > 0) {
                return jsonNode.get(0).asText(); // Lấy ảnh đầu tiên
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "default.png"; // Ảnh mặc định nếu không có ảnh
    }
    public List<Product> searchByName(String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCaseAndStatus(keyword, "Hoạt động");
        setFirstImageForProducts(products);
        return products;
    }
}


