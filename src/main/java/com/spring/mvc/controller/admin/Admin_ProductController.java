package com.spring.mvc.controller.admin;

import com.spring.mvc.domain.Product;
import com.spring.mvc.domain.ProductType;
import com.spring.mvc.domain.Seller;
import com.spring.mvc.service.admin.Admin_ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
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
        model.addAttribute("categories", adminProductService.findAllProductTypes());
        model.addAttribute("sellers", adminProductService.findAllSellers());
        return "admin/view/from_product";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam("productTypeId") Long productTypeId,
                              @RequestParam("sellerId") Long sellerId) {

        Optional<ProductType> productType = adminProductService.findProductTypeById(productTypeId);
        Optional<Seller> seller = adminProductService.findSellerById(sellerId);

        productType.ifPresent(product::setProductType);
        seller.ifPresent(product::setSeller);

        product.setStatus("Chờ duyệt");

        adminProductService.saveProduct(product);
        return "redirect:/admin/product/listNoAccept";
    }


    @GetMapping("/delete/{id}")
    @Transactional
    public String deleteProduct(@PathVariable("id") Long id) {
        adminProductService.deleteProduct(id);
        return "redirect:/admin/product/listNoAccept";
    }

    @GetMapping("/list")
    public String listProducts(Model model) {
        model.addAttribute("products", adminProductService.findApprovedProducts());
        model.addAttribute("pendingProducts", adminProductService.findPendingProducts()); // Add pendingProducts
        return "admin/view/manager_product";
    }

    @PostMapping("/approve/{id}")
    @Transactional
    public String approveProduct(@PathVariable("id") Long id) {
        adminProductService.approveProduct(id);
        return "redirect:/admin/product/listNoAccept";
    }

    @PostMapping("/reject/{id}")
    @Transactional
    public String rejectProduct(@PathVariable("id") Long id) {
        adminProductService.deleteProduct(id);
        return "redirect:/admin/product/listNoAccept";
    }

    @GetMapping("/listNoAccept")
    public String showApproveProductList(Model model) {
        model.addAttribute("pendingProducts", adminProductService.findPendingProducts());
        model.addAttribute("products", adminProductService.findApprovedProducts()); // Add approved products
        return "admin/view/manager_accept_product";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
        Optional<Product> productOptional = adminProductService.findProductById(id);
        return productOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}