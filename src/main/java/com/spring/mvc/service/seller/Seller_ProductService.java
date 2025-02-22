package com.spring.mvc.service.seller;

import com.spring.mvc.domain.Product;
import com.spring.mvc.domain.Seller;
import com.spring.mvc.repository.seller.Seller_ProductReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Seller_ProductService {
    @Autowired
    private Seller_ProductReponsitory productRepository;

    public List<Product> getAllProductsBySeller(Seller seller) {
        return productRepository.findBySeller(seller);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public List<Product> searchProductsByNameOrType(String query, Seller seller) {
        return productRepository.findByNameContainingOrProductType_NameContainingAndSeller(query, seller);
    }
    public List<Product> getApprovedProductsBySeller(Seller seller) {
        return productRepository.findBySellerAndStatus(seller, "Đã duyệt");
    }


}
