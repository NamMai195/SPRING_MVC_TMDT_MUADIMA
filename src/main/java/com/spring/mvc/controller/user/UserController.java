package com.spring.mvc.controller.user;

import com.spring.mvc.dto.user.UserRegisterDTO;
import com.spring.mvc.dto.user.UserLoginDTO;
import com.spring.mvc.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // Xử lý đăng ký
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@ModelAttribute UserRegisterDTO userDTO) {
        Map<String, String> result = userService.registerUser(userDTO);

        if ("success".equals(result.get("status"))) {
            result.put("redirect", "/login"); // Chuyển hướng đến trang đăng nhập
        }

        return ResponseEntity.ok(result);
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@ModelAttribute UserLoginDTO userDTO, HttpSession session) {
        Map<String, String> result = userService.loginUser(userDTO);

        if ("success".equals(result.get("status"))) {
            session.setAttribute("user", result.get("name")); // Lưu tên vào session
            result.put("redirect", "/"); // Chuyển hướng về trang chủ
        }

        return ResponseEntity.ok(result);
    }

    // Kiểm tra thông tin người dùng đang đăng nhập
    @GetMapping("/current-user")
    public ResponseEntity<Map<String, String>> getCurrentUser(HttpSession session) {
        String userName = (String) session.getAttribute("user");
        return ResponseEntity.ok(Map.of("name", userName != null ? userName : ""));
    }

    // Xử lý đăng xuất
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Đăng xuất thành công!");
    }
}
