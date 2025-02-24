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

@Service
@Transactional
public class Admin_ProductService {

    @Autowired
    private Admin_ProductRepository adminProductRepository;

    @Autowired
    private Admin_SellerRepository adminSellerRepository;

    @Autowired
    private Admin_ProductTypeRepository adminProductTypeRepository; // Inject the product type repository

    public List<Product> findAllProducts() {
        return adminProductRepository.findAll();
    }

    public List<Product> findApprovedProducts() {
        return adminProductRepository.findByStatus("Hoạt động");
    }

    public List<Product> findPendingProducts() {
        return adminProductRepository.findByStatus("Chưa duyệt");
    }

    public Optional<Product> findProductById(Long id) {
        return adminProductRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return adminProductRepository.save(product);
    }

    public void deleteProduct(Long id) {
        adminProductRepository.deleteById(id);
    }

    public List<Seller> findAllSellers() {
        return adminSellerRepository.findAll();
    }

    public List<ProductType> findAllProductTypes() {
        return adminProductTypeRepository.findAll();
    }

    public void approveProduct(Long id) {
        Product product = adminProductRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
        product.setStatus("Hoạt động");
        adminProductRepository.save(product);
    }


}