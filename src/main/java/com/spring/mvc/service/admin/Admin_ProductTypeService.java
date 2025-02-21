package com.spring.mvc.service.admin;

import com.spring.mvc.domain.ProductType;
import com.spring.mvc.repository.admin.Admin_ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class Admin_ProductTypeService {

    @Autowired
    private Admin_ProductTypeRepository adminProductTypeRepository;

    public List<ProductType> findAllProductTypes() {
        return adminProductTypeRepository.findAll();
    }

    public Optional<ProductType> findProductTypeById(Long id) {
        return adminProductTypeRepository.findById(id);
    }

    public ProductType saveProductType(ProductType productType) {
        return adminProductTypeRepository.save(productType);
    }

    public void deleteProductType(Long id) {
        adminProductTypeRepository.deleteById(id);
    }

}