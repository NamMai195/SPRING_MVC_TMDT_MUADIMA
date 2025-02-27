package com.spring.mvc.controller.seller;

import com.spring.mvc.domain.Seller;
import com.spring.mvc.repository.seller.Seller_AccountRepository;
import com.spring.mvc.service.seller.Seller_EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Optional;
import java.util.Random;

@Controller
public class Seller_AccountController {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private Seller_AccountRepository sellerRepository;

    @Autowired
    private Seller_EmailService sellerEmailService;

    // Register
    @GetMapping("/registerseller")
    public String SellerRegister() {

        return "/seller/view/register_seller";
    }

    @PostMapping("/register")
    public String registerSeller(@RequestParam("name") String name,
                                 @RequestParam("password") String password,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 @RequestParam("email") String email,
                                 @RequestParam("sdt") String sdt,
                                 @RequestParam("cccd") String cccd,
                                 RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/registerseller";
        }

        if (sellerRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
            return "redirect:/registerseller";
        }
        if (sellerRepository.findBySdt(sdt).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Số điện thoại đã tồn tại!");
            return "redirect:/registerseller";
        }
        if (sellerRepository.findByCccd(cccd).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "CCCD đã tồn tại!");
            return "redirect:/registerseller";
        }

        Seller newSeller = new Seller();
        newSeller.setName(name);
        newSeller.setPassword(passwordEncoder.encode(password));
        newSeller.setEmail(email);
        newSeller.setSdt(sdt);
        newSeller.setCccd(cccd);
        newSeller.setStatus(false);

        sellerRepository.save(newSeller);

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng chờ xét duyệt.");
        return "redirect:/registerseller";
    }

    // login
    @GetMapping("/sellerlogin")
    public String showLoginForm() {
        return "/seller/view/login_seller";
    }


    @PostMapping("/loginseller")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<Seller> sellerOptional = sellerRepository.findByEmail(email);
        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();
            if (passwordEncoder.matches(password, seller.getPassword())) {
                session.setAttribute("loggedInSeller", seller);
                redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
                return "redirect:/seller";
            } else {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu không đúng!");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Email không tồn tại!");
        }
        return "redirect:/sellerlogin";
    }

    // logout
    @GetMapping("/sellerlogout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/sellerlogin";
    }


    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        Seller seller = (Seller) session.getAttribute("loggedInSeller");
        if (seller != null) {
            model.addAttribute("seller", seller);
            return "/seller/view/manager_profile";
        }
        return "redirect:/sellerlogin";
    }

    //  edit profile
    @PostMapping("/profile")
    public String saveProfile(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("sdt") String sdt,
            @RequestParam("address") String address,
            @RequestParam("cccd") String cccd,
            @RequestParam("name_shop") String nameShop,
            @RequestParam(value = "image", required = false) MultipartFile image,
            HttpSession session, RedirectAttributes redirectAttributes) {

        Seller seller = (Seller) session.getAttribute("loggedInSeller");
        if (seller == null) {
            return "redirect:/sellerlogin";
        }

        // Kiểm tra các thông tin khác trước khi cập nhật
        Optional<Seller> existingSellerBySdt = sellerRepository.findBySdt(sdt);
        if (existingSellerBySdt.isPresent() && !existingSellerBySdt.get().getId().equals(seller.getId())) {
            redirectAttributes.addFlashAttribute("error", "Số điện thoại đã được sử dụng!");
            return "redirect:/profile";
        }

        Optional<Seller> existingSellerByCccd = sellerRepository.findByCccd(cccd);
        if (existingSellerByCccd.isPresent() && !existingSellerByCccd.get().getId().equals(seller.getId())) {
            redirectAttributes.addFlashAttribute("error", "CCCD đã tồn tại!");
            return "redirect:/profile";
        }

        Optional<Seller> existingSellerByShop = sellerRepository.findByNameShop(nameShop);
        if (existingSellerByShop.isPresent() && !existingSellerByShop.get().getId().equals(seller.getId())) {
            redirectAttributes.addFlashAttribute("error", "Tên cửa hàng đã tồn tại!");
            return "redirect:/profile";
        }

        // Cập nhật thông tin người bán
        seller.setName(name);
        seller.setSdt(sdt);
        seller.setAddress(address);
        seller.setCccd(cccd);
        seller.setNameShop(nameShop);

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
                seller.setImage(fileName);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Lỗi khi tải ảnh lên!");
                return "redirect:/profile";
            }
        }

        // Lưu seller vào database
        sellerRepository.save(seller);

        redirectAttributes.addFlashAttribute("success", "Cập nhật hồ sơ thành công!");
        return "redirect:/profile";
    }



    // quên mk
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "/seller/view/forgotpass_seller";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        Optional<Seller> sellerOptional = sellerRepository.findByEmail(email);
        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();
            String tempPassword = generateRandomPassword();
            seller.setPassword(passwordEncoder.encode(tempPassword));
            sellerRepository.save(seller);

            String subject = "Khôi phục mật khẩu";
            String message = "Mật khẩu tạm thời của bạn là: " + tempPassword;
            sellerEmailService.sendEmail(email, subject, message);

            model.addAttribute("success", "Mật khẩu tạm thời đã được gửi đến email của bạn!");
        } else {
            model.addAttribute("error", "Email không tồn tại trong hệ thống!");
        }

        return "/seller/view/forgotpass_seller";
    }

    private String generateRandomPassword() {
        int length = 8;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }


    // Mở trang đổi mật khẩu (chỉ hiển thị nút gửi OTP ban đầu)
    @GetMapping("/change-password")
    public String changePassword(HttpSession session, Model model) {
        Seller seller = (Seller) session.getAttribute("loggedInSeller");
        if (seller == null) {
            return "redirect:/sellerlogin";
        }

        model.addAttribute("showForm", false);
        return "/seller/view/changepass_seller";
    }

    // Gửi OTP
    @PostMapping("/send-otp")
    public String sendOtp(HttpSession session, Model model) {
        Seller seller = (Seller) session.getAttribute("loggedInSeller");
        if (seller == null) {
            return "redirect:/sellerlogin";
        }
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        // Lưu OTP vào session
        session.setAttribute("otp", otp);
        // Gửi OTP qua email
        String subject = "Mã OTP xác nhận đổi mật khẩu";
        String message = "Mã OTP của bạn là: " + otp + ". Vui lòng nhập mã này để đổi mật khẩu.";
        sellerEmailService.sendEmail(seller.getEmail(), subject, message);

        model.addAttribute("success", "Mã OTP đã được gửi đến email của bạn.");
        model.addAttribute("showForm", true); // Hiển thị form nhập OTP và mật khẩu mới
        return "/seller/view/changepass_seller";
    }

    // Xác nhận OTP và đổi mật khẩu
    @PostMapping("/confirm-change-password")
    public String confirmChangePassword(
            @RequestParam("otp") int enteredOtp,
            @RequestParam("newPassword") String newPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Integer sessionOtp = (Integer) session.getAttribute("otp");
        Seller seller = (Seller) session.getAttribute("loggedInSeller");

        if (seller == null) {
            return "redirect:/sellerlogin";
        }

        if (sessionOtp == null || enteredOtp != sessionOtp) {
            redirectAttributes.addFlashAttribute("error", "Mã OTP không đúng. Vui lòng thử lại!");
            return "redirect:/change-password";
        }

        // Mã hóa mật khẩu trước khi lưu
        seller.setPassword(passwordEncoder.encode(newPassword));
        sellerRepository.save(seller);

        redirectAttributes.addFlashAttribute("success", "Mật khẩu đã được thay đổi thành công! Vui lòng đăng nhập lại.");

        // Xóa OTP và đăng xuất
        session.removeAttribute("otp");
        session.removeAttribute("loggedInSeller");


        return "redirect:/sellerlogin";
    }

}
