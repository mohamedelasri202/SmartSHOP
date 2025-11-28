package com.example.smartshop.DTO;

import com.example.smartshop.Models.Enums.PaymentStatus; // Required
import com.example.smartshop.Models.Enums.PaymentType; // Required
import com.example.smartshop.Models.Enums.PaymentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {


    private Long id;
    private Long commandeId;
    private PaymentStatus status;


    private BigDecimal amount;
    private PaymentType paymentType;


    private Date paymentDate;
    private Date collectionDate;


    private String reference;
    private String bankName;
    private Date dueDate;
}