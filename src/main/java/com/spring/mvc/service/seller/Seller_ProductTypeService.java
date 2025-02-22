package com.spring.mvc.service.seller;

import com.spring.mvc.domain.ProductType;
import com.spring.mvc.repository.seller.Seller_ProductTypeReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Seller_ProductTypeService {

    @Autowired
    private Seller_ProductTypeReponsitory productTypeRepository;

    public List<ProductType> getAllProductTypes() {
        return productTypeRepository.findAll();
    }

    public void saveProductType(ProductType productType) {
        productTypeRepository.save(productType);
    }

    public Optional<ProductType> findByName(String name) {
        return productTypeRepository.findByName(name);
    }
}
