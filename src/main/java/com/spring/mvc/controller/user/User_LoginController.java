package com.spring.mvc.controller.user;


import com.spring.mvc.domain.User;

import com.spring.mvc.repository.user.User_UserRepository;
import com.spring.mvc.service.user.UserService;
import com.spring.mvc.service.user.User_LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
@Controller
public class User_LoginController {
    @Autowired
    private User_LoginService userService;

    @Autowired
    private User_UserRepository userRepository;
    // login
    @GetMapping("/login")
    public String showLoginForm() {
        return "/user/view/login";
    }
    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getPassword().equals(password)) {
                session.setAttribute("loggedInUser", user);
                return "user/view/index"; //đăng nhập xong về trang
            } else {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu không đúng!");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Email không tồn tại!");
        }
        return "redirect:/login";
    }
    // logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "user/view/login";
    }
}
