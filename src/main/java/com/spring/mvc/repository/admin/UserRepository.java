package com.spring.mvc.repository.admin;

import com.spring.mvc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User save(User user);

    void deleteById(long id);

    List<User> findAll();

    User findById(long id);
}
