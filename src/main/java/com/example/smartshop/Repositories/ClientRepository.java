package com.example.smartshop.Repositories;

import com.example.smartshop.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.LinkOption;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
