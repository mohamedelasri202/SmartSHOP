package com.example.smartshop.DTO;

import com.example.smartshop.Models.Enums.LoyaltyLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private Long id;
    private Long userId;
    private String name;
    private String email;
    private LoyaltyLevel loyaltyLevel;
    private Integer totalOrders;
    private BigDecimal totalSpent;


}
