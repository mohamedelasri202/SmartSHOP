package com.example.smartshop.Models;

import com.example.smartshop.Models.Enums.PaymentStatus; // Assuming this Enum exists
import com.example.smartshop.Models.Enums.PaymentType; // Assuming this Enum exists
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal; // Import for financial accuracy
import java.util.Date;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;


    private Integer paymentNumber;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private Date paymentDate;
    private Date collectionDate;


    private String reference;
    private String bankName;
    private Date dueDate;
}