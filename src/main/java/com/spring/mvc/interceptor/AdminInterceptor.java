package com.spring.mvc.interceptor;

import com.spring.mvc.domain.Admin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;  // Import ModelAndView

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession();
        Admin admin = (Admin) session.getAttribute("loggedInAdmin");

        if (admin == null) {
            response.sendRedirect("/admin/login"); // Redirect to login page
            return false; // Stop further processing
        }

        return true; // Allow the request to proceed
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws  Exception{
        //xử lý logic sau controller nhưng trước khi view được render.
        //thường được sử dụng để thêm các thuộc tính chung vào model
        //hoặc ghi log các hoạt động
    }
    @Override
    public  void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{
        //xử lý logic sau khi view được render
        //giải phóng tài nguyên, xử lý ngoại lệ
        //thường dùng để đóng kết nối, đóng file
    }

}