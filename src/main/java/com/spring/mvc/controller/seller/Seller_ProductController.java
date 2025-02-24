package com.spring.mvc.controller.seller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mvc.domain.Product;
import com.spring.mvc.domain.ProductType;
import com.spring.mvc.domain.Seller;
import com.spring.mvc.service.seller.Seller_ProductService;
import com.spring.mvc.service.seller.Seller_ProductTypeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class Seller_ProductController {
    @Autowired
    private Seller_ProductService productService;

    @Autowired
    private Seller_ProductTypeService productTypeService;

    @GetMapping("/products")
    public String listProducts(Model model, HttpSession session) {
        Seller seller = (Seller) session.getAttribute("loggedInSeller");
        if (seller == null) {
            return "redirect:/sellerlogin";
        }
        model.addAttribute("products", productService.getAllProductsBySeller(seller));
        model.addAttribute("productTypes", productTypeService.getAllProductTypes());
        return "/seller/view/manager_product";
    }

    @PostMapping("/addProduct")
    public String addProduct(
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("quantity") int quantity,
            @RequestParam("type_id") Long typeId,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("describe") String describe,
            HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {

        Seller seller = (Seller) session.getAttribute("loggedInSeller");
        if (seller == null) {
            return "redirect:/sellerlogin";
        }

        // Lấy đường dẫn thư mục uploads theo hệ điều hành
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                // Xóa ký tự đặc biệt khỏi tên file
                String originalFileName = file.getOriginalFilename();
                String cleanFileName = originalFileName.replaceAll("[^a-zA-Z0-9.]", "_");
                String fileName = System.currentTimeMillis() + "_" + cleanFileName;

                // Lưu file
                File destination = new File(uploadDir + fileName);
                file.transferTo(destination);
                imagePaths.add(fileName);
            }
        }

        // Chuyển danh sách ảnh thành JSON để lưu vào database
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonImages = objectMapper.writeValueAsString(imagePaths);

        // Tạo sản phẩm mới
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setSeller(seller);
        product.setProductType(new ProductType(typeId, "", false));
        product.setImage(jsonImages); // Lưu JSON vào DB
        product.setDescribe(describe);
        product.setStatus("Chờ duyệt");

        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm sản phẩm thành công! Vui lòng đợi duyệt.");
        return "redirect:/products";
    }


    @PostMapping("/addProductType")
    public String addProductType(@RequestParam("name_type") String nameType, RedirectAttributes redirectAttributes) {
        Optional<ProductType> existingType = productTypeService.findByName(nameType);
        if (existingType.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Loại sản phẩm đã tồn tại. Vui lòng chọn loại đã có.");
        } else {
            ProductType productType = new ProductType();
            productType.setName(nameType);
            productTypeService.saveProductType(productType);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm loại sản phẩm thành công!");
        }
        return "redirect:/products";
    }

    // Hiển thị danh sách sản phẩm trong manager_product_list
    @GetMapping("/manager_product_list")
    public String showProductList(Model model, HttpSession session) {
        Seller seller = (Seller) session.getAttribute("loggedInSeller");
        if (seller == null) {
            return "redirect:/sellerlogin";
        }
        model.addAttribute("products", productService.getAllProductsBySeller(seller));
        return "/seller/view/manager_product_list";
    }

    @PostMapping("/editProduct")
    public String editProduct(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("quantity") int quantity,
            @RequestParam("describe") String describe,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            RedirectAttributes redirectAttributes) throws IOException {

        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(name);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setDescribe(describe);

            // Lấy đường dẫn thư mục uploads theo hệ điều hành
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            ObjectMapper objectMapper = new ObjectMapper();
            if (images != null && !images.isEmpty()) {
                List<String> imagePaths = new ArrayList<>();
                for (MultipartFile file : images) {
                    if (!file.isEmpty()) {
                        // Xóa ký tự đặc biệt khỏi tên file
                        String originalFileName = file.getOriginalFilename();
                        String cleanFileName = originalFileName.replaceAll("[^a-zA-Z0-9.]", "_");
                        String fileName = System.currentTimeMillis() + "_" + cleanFileName;

                        // Lưu file
                        File destination = new File(uploadDir + fileName);
                        file.transferTo(destination);
                        imagePaths.add(fileName);
                    }
                }

                // Chuyển danh sách ảnh thành JSON để lưu vào database
                String jsonImages = objectMapper.writeValueAsString(imagePaths);
                product.setImage(jsonImages);
            }

            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sản phẩm thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không tồn tại!");
        }
        return "redirect:/manager_product_list";
    }


    @PostMapping("/deleteProduct")
    public String deleteProduct(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isPresent()) {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không tồn tại!");
        }
        return "redirect:/manager_product_list";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model, HttpSession session) {
        Seller seller = (Seller) session.getAttribute("loggedInSeller");
        if (seller == null) {
            return "redirect:/sellerlogin";
        }

        List<Product> products = productService.searchProductsByNameOrType(query, seller);
        model.addAttribute("products", products);
        model.addAttribute("productTypes", productTypeService.getAllProductTypes());
        return "/seller/view/manager_product_list";
    }


    @GetMapping("/seller")
    public String sellerIndex(Model model, HttpSession session) {
        Seller seller = (Seller) session.getAttribute("loggedInSeller");
        if (seller == null) {
            return "redirect:/sellerlogin";
        }

        List<Product> approvedProducts = productService.getApprovedProductsBySeller(seller);

        // Chuyển đổi JSON image thành danh sách ảnh trong Java
        ObjectMapper objectMapper = new ObjectMapper();
        for (Product product : approvedProducts) {
            try {
                List<String> images = objectMapper.readValue(product.getImage(), new TypeReference<List<String>>() {});
                if (!images.isEmpty()) {
                    product.setFirstImage(images.get(0)); // Lưu ảnh đầu tiên vào thuộc tính riêng
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                product.setFirstImage("/default-image.jpg"); // Nếu lỗi thì hiển thị ảnh mặc định
            }
        }

        model.addAttribute("products", approvedProducts);
        return "/seller/view/index";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Chuyển JSON ảnh thành danh sách ảnh
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> images = new ArrayList<>();
            try {
                images = objectMapper.readValue(product.getImage(), new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            product.setFirstImage(images.isEmpty() ? "/default-image.jpg" : images.get(0));
            model.addAttribute("product", product);
            model.addAttribute("images", images);

            return "/seller/view/product_detail"; // Trả về trang Thymeleaf
        }
        return "redirect:/seller"; // Nếu không tìm thấy sản phẩm, quay lại trang chủ
    }

}
