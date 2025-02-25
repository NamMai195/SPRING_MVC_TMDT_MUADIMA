/*
// UserController.java
package com.spring.mvc.controller.user;

import com.spring.mvc.domain.User;
import com.spring.mvc.dto.user.UserLoginDTO;
import com.spring.mvc.dto.user.UserRegisterDTO;
import com.spring.mvc.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@ModelAttribute UserRegisterDTO userDTO) {
        Map<String, String> result = userService.registerUser(userDTO);
        if ("success".equals(result.get("status"))) {
            result.put("redirect", "/login");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@ModelAttribute UserLoginDTO userDTO, HttpSession session) {
        Map<String, Object> result = userService.loginUser(userDTO);
        if ("success".equals(result.get("status"))) {
            session.setAttribute("loggedInUser", result.get("user"));
            result.put("redirect", "/");
        }
        //Kiểm tra product có dữ liệu ko
        System.out.println(result);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate();
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Đăng xuất thành công!");
        response.put("redirect", "/user/login");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("user", loggedInUser);
        return "client/view/user-profile";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User updatedUser,
                             @RequestParam("imageFile") MultipartFile image,
                             HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            if (image != null && !image.isEmpty()) {
                try {
                    // Định danh file mới
                    String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

                    // Thư mục lưu ảnh (ngoài project)
                    String uploadDir = new File("uploads/").getAbsolutePath();
                    File uploadPath = new File(uploadDir);

                    if (!uploadPath.exists()) {
                        uploadPath.mkdirs();
                    }

                    // Lưu ảnh vào thư mục ngoài project
                    File file = new File(uploadPath, fileName);
                    image.transferTo(file);

                    // Cập nhật ảnh mới trong database
                    loggedInUser.setImage(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu ảnh!");
                    return "redirect:/user/profile";
                }
            }

            // Cập nhật thông tin người dùng
            loggedInUser.setName(updatedUser.getName());
            loggedInUser.setEmail(updatedUser.getEmail());
            loggedInUser.setAddress(updatedUser.getAddress());
            loggedInUser.setSdt(updatedUser.getSdt());

            userService.updateUser(loggedInUser.getId(), loggedInUser);
            session.setAttribute("loggedInUser", loggedInUser);

            // Gửi thông báo thành công
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
            return "redirect:/user/profile";
        }

        // Gửi thông báo lỗi nếu không tìm thấy user
        redirectAttributes.addFlashAttribute("error", "Lỗi! Người dùng không tồn tại.");
        return "redirect:/user/profile";
    }
}*/
