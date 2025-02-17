package com.spring.mvc.repository.user;

import com.spring.mvc.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository2 extends JpaRepository<Admin, Long> {

}
