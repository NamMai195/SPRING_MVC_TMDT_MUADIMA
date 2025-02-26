package com.spring.mvc.service.admin;

import com.spring.mvc.domain.Product;
import com.spring.mvc.domain.ProductType;
import com.spring.mvc.domain.Seller;

import com.spring.mvc.repository.admin.Admin_ProductRepository;
import com.spring.mvc.repository.admin.Admin_ProductTypeRepository;
import com.spring.mvc.repository.admin.Admin_SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
//No need for streams: import java.util.stream.Collectors;

@Service
public class Admin_ProductService {

    @Autowired
    private Admin_ProductRepository productRepository;
    @Autowired
    private Admin_ProductTypeRepository productTypeRepository;
    @Autowired
    private Admin_SellerRepository sellerRepository;


    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findApprovedProducts() {
        return productRepository.findByStatus("Hoạt động"); // Corrected
    }

    public List<Product> findPendingProducts() {
        return productRepository.findByStatus("Chờ duyệt"); // Corrected
    }
    @Transactional
    public void approveProduct(Long id) {
        productRepository.findById(id).ifPresent(product -> {
            product.setStatus("Hoạt động"); // Approve the product (set status)
            productRepository.save(product);
        });
    }

    public Optional<ProductType> findProductTypeById(Long id) {
        return productTypeRepository.findById(id);
    }

    public Optional<Seller> findSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    public List<ProductType> findAllProductTypes() {
        return productTypeRepository.findAll();
    }

    public List<Seller> findAllSellers() {
        return sellerRepository.findAll();  // Or filter, if needed
    }
}