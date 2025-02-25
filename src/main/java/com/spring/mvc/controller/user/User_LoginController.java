package com.spring.mvc.controller.user;


import com.spring.mvc.domain.Seller;
import com.spring.mvc.domain.User;

import com.spring.mvc.repository.user.User_UserRepository;
import com.spring.mvc.service.seller.Seller_EmailService;
import com.spring.mvc.service.user.User_LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

@Controller
public class User_LoginController {
    @Autowired
    private User_LoginService userService;

    @Autowired
    private User_UserRepository userRepository;
    // login
    @GetMapping("/login")
    public String showLoginForm() {
        return "user/view/login";
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
                return "redirect:/"; //đăng nhập xong về trang
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

    // Register
    @GetMapping("/registeruser")
    public String SellerRegister() {

        return "user/view/register_user";
    }

    @PostMapping("/registeruser")
    public String registerSeller(@RequestParam("name") String name,
                                 @RequestParam("password") String password,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 @RequestParam("email") String email,
                                 @RequestParam("sdt") String sdt,
                                 RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/registeruser";
        }

        if (userRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
            return "redirect:/registeruser";
        }
        if (userRepository.findBySdt(sdt).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Số điện thoại đã tồn tại!");
            return "redirect:/registeruser";
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setSdt(sdt);
        newUser.setStatus(true);
        userRepository.save(newUser);

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công!.");
        return "redirect:/registeruser";
    }


    // edit profile
    @GetMapping("/profileuser")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            model.addAttribute("user", user);
            return "user/view/user_profile";
        }
        return "redirect:/login";
    }

    @PostMapping("/profileuser")
    public String saveProfile(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("sdt") String sdt,
            @RequestParam("address") String address,
            @RequestParam(value = "image", required = false) MultipartFile image,
            HttpSession session, RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/profileuser";
        }

        // Kiểm tra các thông tin khác trước khi cập nhật
        Optional<User> existingUserBySdt = userRepository.findBySdt(sdt);
        if (existingUserBySdt.isPresent() && !existingUserBySdt.get().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "Số điện thoại đã được sử dụng!");
            return "redirect:/profileuser";
        }


        // Cập nhật thông tin người bán
        user.setName(name);
        user.setSdt(sdt);
        user.setAddress(address);

        // Xử lý upload ảnh
        if (image != null && !image.isEmpty()) {
            try {
                // Lấy đường dẫn thư mục uploads theo hệ điều hành
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }

                // Xóa ký tự đặc biệt khỏi tên file
                String originalFileName = image.getOriginalFilename();
                String cleanFileName = originalFileName.replaceAll("[^a-zA-Z0-9.]", "_");
                String fileName = System.currentTimeMillis() + "_" + cleanFileName;

                // Lưu file
                File file = new File(uploadDir + fileName);
                image.transferTo(file);

                // Lưu đường dẫn chính xác vào database
                user.setImage(fileName);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Lỗi khi tải ảnh lên!");
                return "redirect:/profileuser";
            }
        }

        // Lưu seller vào database
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Cập nhật hồ sơ thành công!");
        return "redirect:/profileuser";
    }


    @Autowired
    Seller_EmailService sellerEmailService;

    // quên mk
    @GetMapping("/forgotpassword")
    public String showForgotPasswordForm() {
        return "user/view/forgotpass_user";
    }

    @PostMapping("/forgotpassword")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String password = user.getPassword();

            String subject = "Khôi phục mật khẩu";
            String message = "Mật khẩu của bạn là: " + password;

            sellerEmailService.sendEmail(email, subject, message);

            model.addAttribute("success", "Mật khẩu đã được gửi đến email của bạn!");
        } else {
            model.addAttribute("error", "Email không tồn tại trong hệ thống!");
        }

        return "user/view/forgotpass_user";
    }



    // Mở trang đổi mật khẩu (chỉ hiển thị nút gửi OTP ban đầu)
    @GetMapping("/changepassword")
    public String changePassword(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("showForm", false);
        return "user/view/changepass_user";
    }

    // Gửi OTP
    @PostMapping("/sendotp")
    public String sendOtp(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);

        // Lưu OTP vào session
        session.setAttribute("otp", otp);

        // Gửi OTP qua email
        String subject = "Mã OTP xác nhận đổi mật khẩu";
        String message = "Mã OTP của bạn là: " + otp + ". Vui lòng nhập mã này để đổi mật khẩu.";

        sellerEmailService.sendEmail(user.getEmail(), subject, message);

        model.addAttribute("success", "Mã OTP đã được gửi đến email của bạn.");
        model.addAttribute("showForm", true); // Hiển thị form nhập OTP và mật khẩu mới
        return "user/view/changepass_user";
    }

    // Xác nhận OTP và đổi mật khẩu
    @PostMapping("/confirmchangepassword")
    public String confirmChangePassword(
            @RequestParam("otp") int enteredOtp,
            @RequestParam("newPassword") String newPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Integer sessionOtp = (Integer) session.getAttribute("otp");
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        if (sessionOtp == null || enteredOtp != sessionOtp) {
            redirectAttributes.addFlashAttribute("error", "Mã OTP không đúng. Vui lòng thử lại!");
            return "redirect:/change-password";
        }

        // Đổi mật khẩu
        user.setPassword(newPassword);
        userRepository.save(user);

        // Xóa OTP và đăng xuất
        session.removeAttribute("otp");
        session.removeAttribute("loggedInUser");

        redirectAttributes.addFlashAttribute("success", "Mật khẩu đã được thay đổi thành công! Vui lòng đăng nhập lại.");
        return "redirect:/login";
    }

}
