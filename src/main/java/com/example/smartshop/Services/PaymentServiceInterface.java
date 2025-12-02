package com.example.smartshop.Services;

import com.example.smartshop.DTO.PaymentDto;
import com.example.smartshop.Models.Enums.PaymentStatus;

public interface PaymentServiceInterface {



    PaymentDto recordPayment(Long commandeId ,PaymentDto paymentDto);
    PaymentDto updatePaymentStatus(Long commandeId , PaymentStatus paymentStatus);

}
