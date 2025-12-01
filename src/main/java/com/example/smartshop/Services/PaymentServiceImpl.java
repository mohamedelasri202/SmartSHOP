package com.example.smartshop.Services;

import com.example.smartshop.DTO.PaymentDto;
import com.example.smartshop.Exceptions.BusinessValidationException;
import com.example.smartshop.Exceptions.ResourceNotFoundException;
import com.example.smartshop.Mapper.PaymentMapper;
import com.example.smartshop.Models.Commande;
import com.example.smartshop.Models.Enums.OrderStatus;
import com.example.smartshop.Models.Enums.PaymentStatus;
import com.example.smartshop.Models.Enums.PaymentType;
import com.example.smartshop.Models.Payment;
import com.example.smartshop.Repositories.CommandeRepository;
import com.example.smartshop.Repositories.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class PaymentServiceImpl implements PaymentServiceInterface{

    private final CommandeRepository commandeRepository;
    private final PaymentMapper  paymentMapper;
    private final PaymentRepository paymentRepository;
    public PaymentServiceImpl(CommandeRepository commandeRepository, PaymentMapper paymentMapper, PaymentRepository paymentRepository) {
        this.commandeRepository = commandeRepository;
        this.paymentMapper = paymentMapper;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public PaymentDto recordPayment(Long commandeId, PaymentDto paymentDto) {

        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + commandeId));

        if (commande.getOrderStatus() != OrderStatus.PENDING) {
            throw new BusinessValidationException("Cannot record payment. Order status is: " + commande.getOrderStatus());
        }


        Payment paymentToSave = paymentMapper.toEntity(paymentDto);




        if (paymentToSave.getPaymentType() == PaymentType.CASH) {
            if (paymentToSave.getAmount().compareTo(new BigDecimal("20000")) > 0) {
                throw new BusinessValidationException("Cash payment exceeds the legal limit of 20,000 DH.");
            }

            paymentToSave.setStatus(PaymentStatus.RECEIVED);
            paymentToSave.setPaymentDate(new Date());
            paymentToSave.setCollectionDate(new Date());
        }

        else if (paymentToSave.getPaymentType() == PaymentType.CHECK) {
            paymentToSave.setStatus(PaymentStatus.WAITING);
            paymentToSave.setPaymentDate(new Date());

        }

        else if (paymentToSave.getPaymentType() == PaymentType.BANKTRANSFER) {
            paymentToSave.setStatus(PaymentStatus.RECEIVED);
            paymentToSave.setPaymentDate(new Date());
            paymentToSave.setCollectionDate(new Date());
        }

        paymentToSave.setCommande(commande);


        Payment savedPayment = paymentRepository.save(paymentToSave);


        commande.setRemainingAmount(commande.getRemainingAmount().subtract(paymentToSave.getAmount()));


        commandeRepository.save(commande);


        return paymentMapper.toDto(savedPayment);
    }


    }



