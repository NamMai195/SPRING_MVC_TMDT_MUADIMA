package com.spring.mvc.service.user;

import com.spring.mvc.domain.Product;
import com.spring.mvc.repository.user.ProductRepository; // Assuming you have a ProductRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findThoiTrangProducts(int productTypeId1, int productTypeId2, int productTypeId3, String status) {
        return productRepository.findTop6ByProductType_IdInAndStatus(List.of(productTypeId1, productTypeId2, productTypeId3), status);
    }
}
