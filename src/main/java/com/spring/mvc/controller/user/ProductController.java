package com.spring.mvc.controller.user;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mvc.domain.Product;
import com.spring.mvc.service.user.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/user")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Chuyển JSON ảnh thành danh sách ảnh
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> images = new ArrayList<>();
            try {
                images = objectMapper.readValue(product.getImage(), new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            product.setFirstImage(images.isEmpty() ? "/default.png" : images.get(0));
            model.addAttribute("product", product);
            model.addAttribute("images", images);

            return "/user/view/product_detail"; // Trả về trang Thymeleaf
        }
        return "redirect:/"; // Nếu không tìm thấy sản phẩm, quay lại trang chủ
    }
}


