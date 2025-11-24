package com.example.smartshop.Models;

import com.example.smartshop.Models.Enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;


    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;


    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    private Date date;

//    the total of the line before any discount
    private BigDecimal sousTotalHT;
//    the total of all the discount that would be applied
    private BigDecimal totalDiscountAmount;
//    total with discount and before taxes
    private BigDecimal amountHTAfterDiscount;
    private BigDecimal vat;
    private BigDecimal totalWithTax;


    private String promoCode;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private BigDecimal remainingAmount;
}