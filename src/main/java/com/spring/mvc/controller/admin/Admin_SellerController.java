
package com.spring.mvc.controller.admin;

import com.spring.mvc.domain.Seller;
import com.spring.mvc.service.admin.Admin_SellerService; // You'll need to create this service
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/seller")
public class Admin_SellerController {

    @Autowired
    private Admin_SellerService adminSellerService; // Inject the Seller service

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("seller", new Seller());
        model.addAttribute("isEditMode", false);
        return "admin/view/from_seller";
    }

    @PostMapping("/save")
    public String saveSeller(@ModelAttribute("seller") Seller seller) {
        adminSellerService.saveSeller(seller);
        return "redirect:/admin/seller/list";
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public String deleteSeller(@PathVariable("id") Long id) {
        adminSellerService.deleteSeller(id);
        return "redirect:/admin/seller/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Seller> sellerOptional = adminSellerService.findSellerById(id);
        if (sellerOptional.isPresent()) {
            model.addAttribute("seller", sellerOptional.get());
            model.addAttribute("isEditMode", true);
            return "admin/view/from_seller";
        } else {
            // Handle seller not found
            return "redirect:/admin/seller/list";
        }
    }
    @PostMapping("/approve/{id}")
    public String approveSeller(@PathVariable("id") Long id) {
        adminSellerService.approveSeller(id);
        return "redirect:/admin/seller/listNoAccept";
    }

    @GetMapping("/list")
    public String listSellers(Model model) {
        model.addAttribute("sellers", adminSellerService.findApprovedSellers());
        model.addAttribute("pendingSellers", adminSellerService.findPendingSellers());
        return "admin/view/manager_seler";
    }
    @GetMapping("/listNoAccept")
    public String listSellers_No_accept(Model model) {
        model.addAttribute("sellers", adminSellerService.findAllSellers());
        model.addAttribute("pendingSellers", adminSellerService.findPendingSellers());
        return "admin/view/manager_accept_seller";
    }
}
