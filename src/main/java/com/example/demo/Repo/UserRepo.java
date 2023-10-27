package com.example.demo.Repo;

import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserUsername(String username);
    User findByUserEmail(String email);
}
