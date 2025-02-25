package com.spring.mvc.repository.user;

import com.spring.mvc.domain.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface User_ProductTypeRepository extends JpaRepository<ProductType, Long> {
    List<ProductType> findByStatusTrue(); // Lấy danh sách loại sản phẩm có trạng thái "hoạt động"
}
