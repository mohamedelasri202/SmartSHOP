package com.example.smartshop.Repositories;

import com.example.smartshop.Models.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    Integer id(Long id);
}
