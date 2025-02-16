package com.spring.mvc.controller.seller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "client/view/index"; // Trả về view "index.html" trong thư mục "templates/client/index"
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
    @GetMapping("/login")
    public String Login() {
        return "client/view/login";
    }
    @GetMapping("/register")
    public String Register() {
        return "client/view/register";
    }
    @GetMapping("/admin")
    public String Admin() {
        return "admin/view/index";
    }
    @GetMapping("/admin/user")
    public String Manager_user() {
        return "admin/view/manager_user";
    }
}