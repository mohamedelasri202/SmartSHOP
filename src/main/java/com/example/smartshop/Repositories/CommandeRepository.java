package com.example.smartshop.Repositories;

import com.example.smartshop.Models.Commande;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.function.Function;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
//    Integer id(Long id);
List<Commande> findAllByClientId(Long clientId);


}
