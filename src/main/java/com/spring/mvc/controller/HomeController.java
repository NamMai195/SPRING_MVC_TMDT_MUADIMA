package com.spring.mvc.controller;

import com.spring.mvc.domain.Product;
import com.spring.mvc.service.user.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productService.getActivePhonesAndComputers());
        return "user/view/index"; // Trang chủ
    }
    @GetMapping("/detail/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/"; // Redirect nếu không tìm thấy sản phẩm
        }
        //Kiểm tra product có dữ liệu ko
        System.out.println(product);

        model.addAttribute("product", product);
        return "user/view/product_detail"; // Trả về trang chi tiết sản phẩm
    }
    @GetMapping("/type")
    public String Type() {
        return "user/view/product_type";
    }

//    @Autowired
//    private ProductService productService;
//
//    @GetMapping("/")
//    public String home(Model model) {
//        model.addAttribute("products", productService.getActivePhonesAndComputers());
//        return "client/view/index"; // Trang chủ
//    }
//    @GetMapping("/detail/{id}")
//    public String productDetail(@PathVariable Long id, Model model) {
//        Product product = productService.getProductById(id);
//        if (product == null) {
//            return "redirect:/"; // Redirect nếu không tìm thấy sản phẩm
//        }
//        //Kiểm tra product có dữ liệu ko
//        System.out.println(product);
//
//        model.addAttribute("product", product);
//        return "client/view/product_detail"; // Trả về trang chi tiết sản phẩm
//    }
//    @GetMapping("/test")
//    public String Test() {
//        return "client/view/test"; // Trả về view "index.html" trong thư mục "templates/client/index"
//    }
//    @GetMapping("/type")
//    public String Type() {
//        return "client/view/product_type";
//    }
//    @GetMapping("/user/login")
//    public String Login() {
//        return "client/view/login";
//    }
//    @GetMapping("/user/register")
//    public String Register() {
//        return "client/view/register";
//    }
}