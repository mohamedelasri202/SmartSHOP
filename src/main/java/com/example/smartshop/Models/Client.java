package com.example.smartshop.Models;

import com.example.smartshop.Models.Enums.LoyaltyLevel;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_Id", unique = true, nullable = false)
    private User user;


    private  String name;
    private String email;
    private Integer totalOrders = 0;
    private BigDecimal totalSpent = BigDecimal.ZERO;
    private Date firstOrderDate;
    private Date lastOrderDate;
    @Enumerated(EnumType.STRING)

    private LoyaltyLevel loyaltyLevel = LoyaltyLevel.BASIC;

    @OneToMany(mappedBy = "client")
    private List<Commande>ordersList;



}
