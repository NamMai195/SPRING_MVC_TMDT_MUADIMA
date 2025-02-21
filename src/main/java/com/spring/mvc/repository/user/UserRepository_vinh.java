package com.spring.mvc.repository.user;

import com.spring.mvc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository_vinh extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}


