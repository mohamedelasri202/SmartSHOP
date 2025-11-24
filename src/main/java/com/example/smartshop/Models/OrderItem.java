package com.example.smartshop.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class OrderItem {
    @Id
    @GeneratedValue
    public Long id;
    private Long productId;
    private Long quantity;
    private Double unitPrice;
    private Double lineTotal;



}
