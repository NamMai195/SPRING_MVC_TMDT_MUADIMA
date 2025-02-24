package com.spring.mvc.config;

import com.spring.mvc.interceptor.AdminInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    AdminInterceptor adminInterceptor;



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Lấy đường dẫn thư mục uploads
        Path uploadDir = Paths.get("uploads");
        String uploadPath = uploadDir.toAbsolutePath().toUri().toString();

        // Cấu hình Spring Boot để phục vụ ảnh từ thư mục này
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**")          // Apply to all URLs under /admin
                .excludePathPatterns("/admin/login", "/admin/forgot-password", "/admin/reset-password"); // Exclude login, forgot password and reset password
    }
}
