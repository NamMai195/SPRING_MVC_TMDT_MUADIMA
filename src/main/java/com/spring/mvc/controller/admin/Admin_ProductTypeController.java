package com.spring.mvc.controller.admin;

import com.spring.mvc.domain.ProductType;
import com.spring.mvc.service.admin.Admin_ProductTypeService; // You'll need to create this service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/product-type")
public class Admin_ProductTypeController {

    @Autowired
    private Admin_ProductTypeService adminProductTypeService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("productType", new ProductType());
        model.addAttribute("isEditMode", false);
        return "admin/view/form_product_types"; // Adjust the template path
    }

    @PostMapping("/save")
    public String saveProductType(@ModelAttribute("productType") ProductType productType) {
        adminProductTypeService.saveProductType(productType);
        return "redirect:/admin/product-type/list"; // Adjust the redirect path
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProductType(@PathVariable("id") Long id) {
        adminProductTypeService.deleteProductType(id);
        return "redirect:/admin/product-type/list"; // Adjust the redirect path
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<ProductType> productTypeOptional = adminProductTypeService.findProductTypeById(id);
        if (productTypeOptional.isPresent()) {
            model.addAttribute("productType", productTypeOptional.get());
            model.addAttribute("isEditMode", true);
            return "admin/view/form_product_types"; // Adjust the template path
        } else {
            // Handle product type not found
            return "redirect:/admin/product-type/list"; // Adjust the redirect path
        }
    }

    @GetMapping("/list")
    public String listProductTypes(Model model) {
        model.addAttribute("productTypes", adminProductTypeService.findAllProductTypes());
        return "admin/view/manager_product_types"; // Adjust the template path
    }
}