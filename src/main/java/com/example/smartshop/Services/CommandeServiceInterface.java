package com.example.smartshop.Services;

import com.example.smartshop.DTO.CommandeDto;

import java.util.List;

public interface CommandeServiceInterface {

    CommandeDto createCommande(CommandeDto commande);
    CommandeDto confirmCommande(Long id);
    List<CommandeDto> getAllOrders();
    List<CommandeDto> getOrdersByClient(Long clientId);
}
