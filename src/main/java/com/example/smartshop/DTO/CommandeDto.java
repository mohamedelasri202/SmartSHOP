package com.example.smartshop.DTO;

import com.example.smartshop.Models.Enums.OrderStatus;
import com.example.smartshop.Models.Enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CommandeDto {

    private Long id;
    private Long clientId;
    private Date date;


    private BigDecimal subtotalHT;
    private BigDecimal totalDiscountAmount;
    private BigDecimal amountHTAfterDiscount;
    private BigDecimal vatAmount;
    private BigDecimal totalTTC;

    private String promoCode;

    private com.example.smartshop.Models.Enums.OrderStatus orderStatus;
    private BigDecimal remainingAmount;


    private List<OrderItemDto> items;
    private List<PaymentDto> payments;
}