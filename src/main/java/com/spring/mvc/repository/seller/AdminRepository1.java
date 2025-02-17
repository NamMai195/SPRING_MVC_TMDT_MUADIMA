package com.spring.mvc.repository.seller;

import com.spring.mvc.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository1 extends JpaRepository<Admin, Long> {

}
