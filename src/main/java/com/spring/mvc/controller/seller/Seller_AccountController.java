package com.spring.mvc.controller.seller;

import com.spring.mvc.domain.Seller;
import com.spring.mvc.repository.seller.Seller_AccountRepository;
import com.spring.mvc.service.seller.Seller_EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

@Controller
public class Seller_AccountController {

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
        newSeller.setPassword(password);
        newSeller.setEmail(email);
        newSeller.setSdt(sdt);
        newSeller.setCccd(cccd);
        newSeller.setStatus(false);

        sellerRepository.save(newSeller);

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng chờ xét duyệt.");
        return "redirect:/registerseller";
    }

    @GetMapping("/sellerlogin")
    public String showLoginForm() {
        return "/seller/view/login_seller";
    }


    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<Seller> sellerOptional = sellerRepository.findByEmail(email);

        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();

            if (!seller.isStatus()) {
                redirectAttributes.addFlashAttribute("error", "Tài khoản bán hàng chưa được duyệt!");
                return "redirect:/sellerlogin";
            }

            if (seller.getPassword().equals(password)) {
                session.setAttribute("loggedInSeller", seller);
                return "redirect:/seller";
            } else {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu không đúng hoặc Email không tồn tại!");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu không đúng hoặc Email không tồn tại!");
        }

        return "redirect:/sellerlogin";
    }


    @GetMapping("/logout")
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

        seller.setName(name);
        seller.setSdt(sdt);
        seller.setAddress(address);
        seller.setCccd(cccd);
        seller.setNameShop(nameShop);

        if (image != null && !image.isEmpty()) {
            try {
                String fileName = image.getOriginalFilename();
                String uploadDir = "src/main/resources/static/assets/img";
                File file = new File(uploadDir + fileName);
                image.transferTo(file);
                seller.setImage("/img/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        sellerRepository.save(seller);
        session.setAttribute("loggedInSeller", seller);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");

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
            String password = seller.getPassword();

            String subject = "Khôi phục mật khẩu";
            String message = "Mật khẩu của bạn là: " + password;

            sellerEmailService.sendEmail(email, subject, message);

            model.addAttribute("success", "Mật khẩu đã được gửi đến email của bạn!");
        } else {
            model.addAttribute("error", "Email không tồn tại trong hệ thống!");
        }

        return "/seller/view/forgotpass_seller";
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

        // Đổi mật khẩu
        seller.setPassword(newPassword);
        sellerRepository.save(seller);

        // Xóa OTP và đăng xuất
        session.removeAttribute("otp");
        session.removeAttribute("loggedInSeller");

        redirectAttributes.addFlashAttribute("success", "Mật khẩu đã được thay đổi thành công! Vui lòng đăng nhập lại.");
        return "redirect:/sellerlogin";
    }

}
