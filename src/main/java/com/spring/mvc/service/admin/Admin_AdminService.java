package com.spring.mvc.service.admin;

import com.spring.mvc.domain.Admin;

import com.spring.mvc.repository.admin.Admin_AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Admin_AdminService {

    @Autowired
    private Admin_AdminRepository adminRepository;

    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public void save(Admin admin) {
        adminRepository.save(admin);
    }

    public Optional<Admin> findById(Long id) {
    return adminRepository.findById(id);
    }

    public Optional<Admin> findByResetToken(String resetToken) {
        return adminRepository.findByResetToken(resetToken); // This line is correct
    }
}