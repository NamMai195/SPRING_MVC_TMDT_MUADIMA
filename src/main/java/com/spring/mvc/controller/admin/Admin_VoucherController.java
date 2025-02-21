package com.spring.mvc.controller.admin;

import com.spring.mvc.domain.Voucher;
import com.spring.mvc.service.admin.Admin_VoucherService; // You'll need to create this service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/voucher")
public class Admin_VoucherController {

    @Autowired
    private Admin_VoucherService adminVoucherService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("voucher", new Voucher());
        model.addAttribute("isEditMode", false);
        model.addAttribute("sellers", adminVoucherService.findAllSellers()); // Add sellers to the model
        return "admin/view/form_voucher";
    }

    @PostMapping("/save")
    public String saveVoucher(@ModelAttribute("voucher") Voucher voucher) {
        adminVoucherService.saveVoucher(voucher);
        return "redirect:/admin/voucher/list";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable("id") Long id) {
        adminVoucherService.deleteVoucher(id);
        return "redirect:/admin/voucher/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Voucher> voucherOptional = adminVoucherService.findVoucherById(id);
        if (voucherOptional.isPresent()) {
            Voucher voucher = voucherOptional.get();
            model.addAttribute("voucher", voucher);
            model.addAttribute("isEditMode", true);
            // Truy cập trực tiếp người bán từ voucher
            model.addAttribute("seller", voucher.getSeller());
            return "admin/view/form_voucher";
        } else {
            return "redirect:/admin/voucher/list";
        }
    }

    @GetMapping("/list")
    public String listVouchers(Model model) {
        model.addAttribute("vouchers", adminVoucherService.findAllVouchers());
        return "admin/view/manager_voucher";
    }
    @GetMapping("/listNoAccept")
    public String showApproveVoucherList(Model model) {
        List<Voucher> pendingVouchers = adminVoucherService.findPendingVouchers();
        model.addAttribute("pendingVouchers", pendingVouchers);
        for (Voucher voucher: pendingVouchers) {
            System.out.println(voucher);
        }
        return "admin/view/manager_accept_voucher";
    }
    @PostMapping("/approve/{id}")
    public String approveVoucher(@PathVariable("id") Long id) {
        adminVoucherService.approveVoucher(id);
        return "redirect:/admin/voucher/approve-list";
    }

}