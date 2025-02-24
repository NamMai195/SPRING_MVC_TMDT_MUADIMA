package com.spring.mvc.service.admin;

import com.spring.mvc.domain.User;
import com.spring.mvc.repository.admin.Admin_UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Admin_UserService {
    @Autowired
    private Admin_UserRepository admin_userRepository;

    public List<User> findAllUser() {
        return this.admin_userRepository.findAll();
    }
    public User saveUser(User user) {
        return this.admin_userRepository.save(user);
    }

    public void deleteUser(Long user) {
        this.admin_userRepository.deleteById(user);
    }

    public User findUserById(Long id) {
        return this.admin_userRepository.findById(id);
    }

    public Object countUsers() {
        return this.admin_userRepository.count();
    }
}
