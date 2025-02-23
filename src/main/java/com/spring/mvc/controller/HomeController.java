package com.spring.mvc.controller;

import com.spring.mvc.domain.Product;
import com.spring.mvc.service.user.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        List<Product> thoiTrangProducts = productService.findThoiTrangProducts(8, 16, 10, "hoat động"); // Get products

        model.addAttribute("thoiTrangProducts", thoiTrangProducts); // Add to model

        return "client/view/index"; // Return the name of your Thymeleaf template (index.html)
    }
    @GetMapping("/detail")
    public String Detail() {
        return "client/view/product_detail"; // Trả về view "index.html" trong thư mục "templates/client/index"
    }
    @GetMapping("/test")
    public String Test() {
        return "client/view/test"; // Trả về view "index.html" trong thư mục "templates/client/index"
    }
    @GetMapping("/type")
    public String Type() {
        return "client/view/product_type";
    }
    @GetMapping("/user/login")
    public String Login() {
        return "client/view/login";
    }
    @GetMapping("/user/register")
    public String Register() {
        return "client/view/register";
    }
}