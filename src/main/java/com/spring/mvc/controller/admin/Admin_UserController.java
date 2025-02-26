package com.spring.mvc.controller.admin;

import com.spring.mvc.domain.User;
import com.spring.mvc.service.admin.Admin_UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Import ResponseEntity
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/user")
public class Admin_UserController {

    @Autowired
    private Admin_UserService adminUserService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("isEditMode", false); // Cái này có thể không cần nữa, vì modal view không có edit
        return "admin/view/from_user";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        adminUserService.saveUser(user);
        return "redirect:/admin/user/list";
    }

    @RequestMapping("/delete/{id}") // Hoặc @GetMapping("/delete/{id}") cũng được, vì đã xử lý bằng AJAX
    @Transactional
    public String deleteUser(@PathVariable("id") Long id) {
        adminUserService.deleteUser(id);
        return "redirect:/admin/user/list";
    }

    // Xóa phương thức showEditForm, không cần thiết nữa
    /*
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
    */

    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", adminUserService.findAllUser());
        return "admin/view/manager_user";
    }

    // Thêm phương thức GET để lấy thông tin user (quan trọng)
    @GetMapping("/get/{id}")
    @ResponseBody // Đánh dấu để Spring biết trả về dữ liệu, không phải view
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        Optional<User> userOptional = Optional.ofNullable(adminUserService.findUserById(id));
        return userOptional.map(ResponseEntity::ok) // Nếu tìm thấy user, trả về OK (200) và user
                .orElseGet(() -> ResponseEntity.notFound().build()); // Nếu không tìm thấy, trả về 404 Not Found
    }
}