package com.example.smartshop.Services;

import com.example.smartshop.DTO.CommandeDto;

public interface CommandeServiceInterface {

    CommandeDto createCommande(CommandeDto commande);
    CommandeDto confirmCommande(Long id);
}
