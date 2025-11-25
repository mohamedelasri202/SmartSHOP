package com.example.smartshop.Models;

import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal unitPrice;
    private Integer availableStock;
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
    private Boolean isDeleted = false;


}
