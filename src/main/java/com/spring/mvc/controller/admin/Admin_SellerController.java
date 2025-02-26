package com.spring.mvc.controller.admin;

import com.spring.mvc.domain.Seller;
import com.spring.mvc.service.admin.Admin_SellerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/seller")
public class Admin_SellerController {

    @Autowired
    private Admin_SellerService adminSellerService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("seller", new Seller());
        model.addAttribute("isEditMode", false);  // Still good practice, even with modal
        return "admin/view/from_seller";
    }

    @PostMapping("/save")
    public String saveSeller(@ModelAttribute("seller") Seller seller) {
        adminSellerService.saveSeller(seller);
        return "redirect:/admin/seller/listNoAccept"; // Redirect to the pending list after saving
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deleteSeller(@PathVariable("id") Long id) {
        adminSellerService.deleteSeller(id);
        return "redirect:/admin/seller/listNoAccept"; // Redirect to pending list after delete
    }
    // No edit form needed

    @PostMapping("/approve/{id}")
    public String approveSeller(@PathVariable("id") Long id) {
        adminSellerService.approveSeller(id);
        return "redirect:/admin/seller/listNoAccept"; // Redirect to pending list (it will be removed on reload)
    }

    @GetMapping("/list")
    public String listSellers(Model model) {
        model.addAttribute("sellers", adminSellerService.findApprovedSellers());
        return "admin/view/manager_seler";
    }

    @GetMapping("/listNoAccept")
    public String listSellersNoAccept(Model model) {
        model.addAttribute("pendingSellers", adminSellerService.findPendingSellers()); // Corrected attribute name
        return "admin/view/manager_accept_seller";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<Seller> getSeller(@PathVariable("id") Long id) {
        Optional<Seller> sellerOptional = adminSellerService.findSellerById(id);
        return sellerOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/lock/{id}")
    @Transactional // Important for database updates
    public String lockSeller(@PathVariable("id") Long id) {
        adminSellerService.lockSeller(id); // You'll need to implement this service method
        return "redirect:/admin/seller/list"; // Redirect back to the seller list
    }

    @PostMapping("/unlock/{id}")
    @Transactional
    public String unlockSeller(@PathVariable("id") Long id) {
        adminSellerService.unlockSeller(id); // You'll need to implement this service method
        return "redirect:/admin/seller/list";
    }
}