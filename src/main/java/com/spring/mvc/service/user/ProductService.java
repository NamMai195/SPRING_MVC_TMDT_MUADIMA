package com.spring.mvc.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mvc.domain.Product;
import com.spring.mvc.repository.user.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Product> getActivePhonesAndComputers() {
        return productRepository.findTop6ByProductTypeIdAndStatus(2L, "Hoạt động").stream()
                .peek(product -> product.setImage(getFirstImage(product.getImage())))
                .collect(Collectors.toList());
    }
    public String getFirstImage(String jsonImages) {
        try {
            List<String> images = objectMapper.readValue(jsonImages, List.class);
            String firstImage = images.isEmpty() ? "/uploads/default.jpg" : images.get(0);
            return firstImage;
        } catch (JsonProcessingException e) {
            return "/uploads/default.jpg";
        }
    }


}

