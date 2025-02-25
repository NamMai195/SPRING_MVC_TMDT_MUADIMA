package com.spring.mvc.service.user;

import com.spring.mvc.domain.ProductType;
import com.spring.mvc.repository.user.User_ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeService {

    @Autowired
    private User_ProductTypeRepository productTypeRepository;

    public List<ProductType> getAllActiveProductTypes() {
        return productTypeRepository.findByStatusTrue(); // Lấy danh sách loại sản phẩm đang hoạt động
    }
}

