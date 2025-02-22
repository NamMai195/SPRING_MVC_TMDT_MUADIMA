package com.spring.mvc.service.user;

import com.spring.mvc.domain.User;
import com.spring.mvc.dto.user.UserLoginDTO;
import com.spring.mvc.dto.user.UserRegisterDTO;
import com.spring.mvc.repository.user.User_UserRepository;
import com.spring.mvc.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private User_UserRepository userRepository;

    // Xử lý đăng ký người dùng
    public Map<String, String> registerUser(UserRegisterDTO userDTO) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            response.put("status", "error");
            response.put("message", "Email đã được sử dụng!");
            return response;
        }

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(PasswordUtils.hashPassword(userDTO.getPassword())); // Mã hóa SHA-256
        user.setAddress(userDTO.getAddress());
        user.setSdt(userDTO.getSdt());
        user.setImage(null);  // Mặc định là null
        user.setStatus(true); // Mặc định là true

        userRepository.save(user);

        response.put("status", "success");
        response.put("message", "Đăng ký thành công!");
        return response;
    }

    // Xử lý đăng nhập người dùng
    public Map<String, Object> loginUser(UserLoginDTO userDTO) {
        Map<String, Object> response = new HashMap<>(); // Thay đổi kiểu dữ liệu

        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
        if (userOptional.isEmpty()) {
            response.put("status", "error");
            response.put("message", "Email không tồn tại!");
            return response;
        }

        User user = userOptional.get();
        if (!PasswordUtils.verifyPassword(userDTO.getPassword(), user.getPassword())) {
            response.put("status", "error");
            response.put("message", "Mật khẩu không đúng!");
            return response;
        }

        response.put("status", "success");
        response.put("message", "Đăng nhập thành công!");
        response.put("user", user); // Lưu đối tượng User vào response
        return response;
    }

    public void updateUser(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setSdt(updatedUser.getSdt());

            // Cập nhật ảnh nếu có
            if (updatedUser.getImage() != null) {
                existingUser.setImage(updatedUser.getImage());
            }

            userRepository.save(existingUser);
        }
    }
}
