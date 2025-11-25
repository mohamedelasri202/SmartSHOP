package com.example.smartshop.Repositories;

import com.example.smartshop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;

public interface UserRepository extends JpaRepository<User, Long> {
}
