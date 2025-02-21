package com.spring.mvc.controller.admin;

import com.spring.mvc.domain.User;
import com.spring.mvc.service.admin.Admin_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private Admin_UserService admin_UserService;

    @GetMapping("/admin")
    public String Admin() {
        return "admin/view/index";
    }

    @GetMapping("/admin/test")
    public String managerTest(Model model) {
        model.addAttribute("user", new User()); // Thêm một User object mới vào model
        return "admin/view/from_user";
    }


}