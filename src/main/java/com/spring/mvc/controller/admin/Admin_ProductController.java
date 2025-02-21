package com.spring.mvc.controller.admin;

import com.spring.mvc.domain.Product;
import com.spring.mvc.domain.ProductType;
import com.spring.mvc.service.admin.Admin_ProductService; // You'll need to create this service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/product")
public class Admin_ProductController {

    @Autowired
    private Admin_ProductService adminProductService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("isEditMode", false);
        // Add categories and sellers to the model
        model.addAttribute("categories", adminProductService.findAllProductTypes());
        List<ProductType> test = adminProductService.findAllProductTypes();
        for (ProductType productType : test) {
            System.out.println(productType);
        }
        model.addAttribute("sellers", adminProductService.findAllSellers());
        return "admin/view/form_product"; // Adjust the template path
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        adminProductService.saveProduct(product);
        return "redirect:/admin/product/list"; // Adjust the redirect path
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        adminProductService.deleteProduct(id);
        return "redirect:/admin/product/list"; // Adjust the redirect path
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Product> productOptional = adminProductService.findProductById(id);
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            model.addAttribute("isEditMode", true);
            // Add categories and sellers to the model
            model.addAttribute("categories", adminProductService.findAllProductTypes());
            model.addAttribute("sellers", adminProductService.findAllSellers());
            return "admin/view/form_product"; // Adjust the template path
        } else {
            // Handle product not found
            return "redirect:/admin/product/list"; // Adjust the redirect path
        }
    }

    @GetMapping("/list")
    public String listProducts(Model model) {
        model.addAttribute("products", adminProductService.findApprovedProducts());
        model.addAttribute("pendingProducts", adminProductService.findPendingProducts());
        return "admin/view/manager_product"; // Adjust the template path
    }

    @PostMapping("/approve/{id}")
    public String approveProduct(@PathVariable("id") Long id) {
        adminProductService.approveProduct(id);
        return "redirect:/admin/product/list"; // Adjust the redirect path
    }
    @GetMapping("/listNoAccept")
    public String showApproveProductList(Model model) {
        model.addAttribute("pendingProducts", adminProductService.findPendingProducts());
        return "admin/view/manager_accept_product";
    }
}