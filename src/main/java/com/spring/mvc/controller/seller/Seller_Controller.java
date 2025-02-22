package com.spring.mvc.controller.seller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class Seller_Controller {
    /*@RequestMapping("/seller")
    public String SellerController() {

        return "/seller/view/index";
    }*/
    @GetMapping("/manager_order")
    public String SellerOrder() {

        return "/seller/view/manager_order";
    }

}