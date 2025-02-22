package com.spring.mvc.controller;

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
    @GetMapping("/user/login")
    public String Login() {
        return "client/view/login";
    }
    @GetMapping("/user/register")
    public String Register() {
        return "client/view/register";
    }
}