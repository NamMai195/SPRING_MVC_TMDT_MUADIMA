package com.spring.mvc.service.seller;

import com.spring.mvc.domain.Admin;
import com.spring.mvc.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerService {
   /* Optional<Seller> login(String email, String password);*/
}
