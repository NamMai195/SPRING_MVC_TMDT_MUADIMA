package com.spring.mvc.controller.admin;

import com.spring.mvc.domain.Admin;
import com.spring.mvc.service.admin.*;
import com.spring.mvc.service.seller.Seller_EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Import for password hashing
import org.springframework.security.crypto.password.PasswordEncoder; // Import PasswordEncoder
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private Admin_AdminService adminService;

    @Autowired
    private Seller_EmailService adminEmailService;  // Corrected: Use Admin_EmailService

    // Password encoder bean (IMPORTANT for security)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private Admin_UserService userService; // Inject UserService

    @Autowired
    private Admin_SellerService sellerService;  // Inject SellerService

    @Autowired
    private Admin_OrderService orderService;

    // Password encoder bean (IMPORTANT for security)


    @GetMapping("")
    public String adminDashboard(Model model, HttpSession session) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("adminName", loggedInAdmin.getName());

        // --- Real Data ---
//        model.addAttribute("totalUsers", userService.countUsers());
//        model.addAttribute("totalSellers", sellerService.countSellers());
//        model.addAttribute("totalOrders", orderService.countOrders());
//        model.addAttribute("totalRevenue", orderService.calculateTotalRevenue()); // Assuming a method like this
//
//
//        // --- Chart Data (Last 7 Days) ---
//        List<String> last7Days = getLast7Days();
//        List<Integer> orderCounts = new ArrayList<>();
//
//        // Get order counts for each of the last 7 days
//        for (String date : last7Days) {
//            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MMM dd"));
//            LocalDateTime startOfDay = localDate.atStartOfDay();
//            LocalDateTime endOfDay = localDate.atTime(23, 59, 59); // End of the day
//            int count = orderService.countOrdersByDateRange(startOfDay, endOfDay);
//            orderCounts.add(count);
//        }
//
//
//        model.addAttribute("chartLabels", last7Days);
//        model.addAttribute("chartData", orderCounts);

        return "admin/view/index";
    }

    // Helper method (Keep this - it's correct)
//    private List<String> getLast7Days() {
//        LocalDate today = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd"); // e.g., "Feb 24"
//        return IntStream.range(0, 7)
//                .mapToObj(i -> today.minusDays(i).format(formatter))
//                .collect(Collectors.toList());
//    }

    @GetMapping("/login")
    public String showAdminLoginForm(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "Invalid Credentials");
        }
        return "admin/view/admin_login";
    }


    @PostMapping("/login")
    public String loginAdmin(@RequestParam("email") String email,
                             @RequestParam("password") String password,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        Optional<Admin> adminOptional = adminService.findByEmail(email);

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            // Use PasswordEncoder for comparison!
            if (passwordEncoder().matches(password, admin.getPassword())) {
                session.setAttribute("loggedInAdmin", admin);
                return "redirect:/admin";
            } else {
                redirectAttributes.addFlashAttribute("error", "Incorrect password!");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Email not found!");
        }

        return "redirect:/admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    @GetMapping("/profile")
    public String showAdminProfile(HttpSession session, Model model) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("admin", loggedInAdmin);
        return "admin/view/admin_profile";
    }



    @PostMapping("/profile")
    public String saveAdminProfile(@RequestParam("name") String name,
                                   @RequestParam("email") String email,
                                   @RequestParam(value = "password", required = false) String password, // Optional password
                                   @RequestParam("id") Long id,
                                   @RequestParam(value = "image", required = false) MultipartFile image,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }


        Optional<Admin> existingAdminOptional = adminService.findById(id);
        if (!existingAdminOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Admin not found!");
            return "redirect:/admin/profile";
        }
        Admin admin = existingAdminOptional.get();

        Optional<Admin> existingAdminByEmail = adminService.findByEmail(email);
        if (existingAdminByEmail.isPresent() && !existingAdminByEmail.get().getId().equals(admin.getId())) {
            redirectAttributes.addFlashAttribute("error", "Email already in use!");
            return "redirect:/admin/profile";
        }

        admin.setName(name);
        admin.setEmail(email);


        if (password != null && !password.isEmpty()) {
            // Hash the new password
            admin.setPassword(passwordEncoder().encode(password));
        }

        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + "/uploads/admin";  // Separate folder for admin images
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }

                String originalFileName = image.getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + originalFileName; // Unique filename
                File file = new File(uploadDir + "/" + fileName); // Use / for paths.
                image.transferTo(file);
                admin.setImage("/uploads/admin/" + fileName); // Correct path
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Error uploading image!");
                return "redirect:/admin/profile";
            }
        }

        adminService.save(admin);
        session.setAttribute("loggedInAdmin", admin); // Update session!
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/admin/profile";
    }
    // --- Forgot Password ---

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "admin/view/admin_fogerpass"; // Corrected template name
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        Optional<Admin> adminOptional = adminService.findByEmail(email);

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            // 1. Generate a secure token
            String token = UUID.randomUUID().toString();

            // 2. Store the token and expiry time in the database
            admin.setResetToken(token);
            admin.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
            adminService.save(admin);

            // 3. Construct the reset link
            String resetLink = "http://localhost:8080/admin/reset-password?token=" + token; // IMPORTANT: Use your actual domain!  And correct port if not 8080

            // 4. Send the email
            String subject = "Password Reset Request";
            String message = "To reset your password, please click the following link:\n\n"
                    + resetLink + "\n\nThis link will expire in 1 hour.";

            adminEmailService.sendEmail(admin.getEmail(), subject, message);  // Corrected: Use adminEmailService

            redirectAttributes.addFlashAttribute("success", "If an account with that email exists, a password reset link has been sent.");
        } else {
            redirectAttributes.addFlashAttribute("error", "If an account with that email exists, a password reset message has been sent.");
        }

        return "redirect:/admin/login";
    }

    // --- Change Password (with OTP) ---  REMOVE THIS ENTIRE SECTION

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        Optional<Admin> adminOptional = adminService.findByResetToken(token); // New repository method!

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            // Check if the token is expired
            if (admin.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("error", "Password reset token has expired.");
                return "redirect:/admin/login";
            }

            model.addAttribute("token", token); // Pass the token to the view
            return "admin/view/admin_ressetpass"; // New template!
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid password reset token.");
            return "redirect:/admin/login";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                RedirectAttributes redirectAttributes) {

        Optional<Admin> adminOptional = adminService.findByResetToken(token);

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            // Check if the token is expired
            if (admin.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("error", "Password reset token has expired.");
                return "redirect:/admin/login";
            }

            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
                return "redirect:/admin/reset-password?token=" + token; //keep token
            }

            // Hash the new password
            admin.setPassword(passwordEncoder().encode(newPassword));

            // Clear the token and expiry
            admin.setResetToken(null);
            admin.setResetTokenExpiry(null);

            adminService.save(admin);

            redirectAttributes.addFlashAttribute("success", "Your password has been reset.  You can now log in.");
            return "redirect:/admin/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid password reset token.");
            return "redirect:/admin/login";
        }
    }

}