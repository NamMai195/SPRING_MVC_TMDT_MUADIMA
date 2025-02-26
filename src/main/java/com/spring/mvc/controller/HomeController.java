package com.spring.mvc.controller;

import com.spring.mvc.domain.Product;
import com.spring.mvc.domain.ProductType;
import com.spring.mvc.service.user.ProductService;
import com.spring.mvc.service.user.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class HomeController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTypeService productTypeService;

    @GetMapping("/")
    public String home(
            @RequestParam(value = "typeId", required = false) Long typeId,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        List<ProductType> productTypes = productTypeService.getAllActiveProductTypes(); // Lấy danh sách loại sản phẩm
        List<Product> products;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Nếu có keyword -> Tìm kiếm sản phẩm theo tên
            products = productService.searchByName(keyword);
        } else if (typeId != null) {
            // Nếu có typeId -> Lọc sản phẩm theo loại
            products = productService.getProductsByType(typeId);
        } else {
            // Nếu không có filter nào -> Lấy toàn bộ sản phẩm có trạng thái "Hoạt động"
            products = productService.getActiveProducts();
        }

        model.addAttribute("productTypes", productTypes);
        model.addAttribute("products", products);
        model.addAttribute("selectedTypeId", typeId);
        model.addAttribute("keyword", keyword); // Lưu keyword để hiển thị lại khi tìm kiếm

        return "user/view/index"; // Trả về trang index hiển thị kết quả
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