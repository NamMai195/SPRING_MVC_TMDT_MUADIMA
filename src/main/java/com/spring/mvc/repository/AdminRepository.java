package com.spring.mvc.repository;

import com.spring.mvc.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin save(Admin admin);

    void deleteById(long id);

    List<Admin> findAll();

    Admin findByid(long id);

    Admin findByUsername(String username);
}
