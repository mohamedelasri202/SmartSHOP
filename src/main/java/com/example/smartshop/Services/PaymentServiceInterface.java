package com.example.smartshop.Services;

import com.example.smartshop.DTO.PaymentDto;

public interface PaymentServiceInterface {
    PaymentDto recordPayment(Long commandeId ,PaymentDto paymentDto);
}
