package com.example.smartshop.Services;

import com.example.smartshop.DTO.PaymentDto;
import com.example.smartshop.Exceptions.ResourceNotFoundException;
import com.example.smartshop.Models.Commande;
import com.example.smartshop.Models.Enums.OrderStatus;
import com.example.smartshop.Repositories.CommandeRepository;

public class PaymentServiceImpl implements PaymentServiceInterface{

    private final CommandeRepository commandeRepository;
    public PaymentServiceImpl(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    public PaymentDto recordPayment(Long commandeId ,PaymentDto paymentDto) {
        Commande commande = commandeRepository.findById(commandeId).orElseThrow(()-> new ResourceNotFoundException("SORRY THERS NO COMMANDE WITH THIS ID "+commandeId)
        );

        if(!commande.getOrderStatus().equals(OrderStatus.PENDING)){
            throw new ResourceNotFoundException("You can't record payment for this order, the Status is "+commande.getOrderStatus());
        }

    }


}
