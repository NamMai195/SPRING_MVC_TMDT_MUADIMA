package com.spring.mvc.controller.admin;

import com.spring.mvc.domain.User;
import com.spring.mvc.service.admin.Admin_UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/user")
public class Admin_UserController {

    @Autowired
    private  Admin_UserService adminUserService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("isEditMode", false);
        return "admin/view/from_user"; // Assuming this is your Thymeleaf template name
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        adminUserService.saveUser(user);
        return "redirect:/admin/user/list";
    }

    @RequestMapping("/delete/{id}")
    @Transactional
    public String deleteUser(@PathVariable("id") Long id) {
        adminUserService.deleteUser(id);
        return "redirect:/admin/user/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<User> userOptional = Optional.ofNullable(adminUserService.findUserById(id));
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            model.addAttribute("isEditMode", true);
            return "admin/view/from_user";
        } else {
            return "redirect:/admin/user/list";
        }
    }

    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", adminUserService.findAllUser());
        return "admin/view/manager_user";
    }
}