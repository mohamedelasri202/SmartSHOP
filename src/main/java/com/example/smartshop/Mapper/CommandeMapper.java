package com.example.smartshop.Mapper;

import com.example.smartshop.DTO.CommandeDto;
import com.example.smartshop.Models.Commande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class})
public interface CommandeMapper {


    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "vat", target = "vat")


    @Mapping(source = "sousTotalHT", target = "subtotalHT")
    @Mapping(source = "totalWithTax", target = "totalTTC")


    @Mapping(source = "items", target = "items")
    @Mapping(source = "payments", target = "payments")

    CommandeDto toDto(Commande commande);




    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "remainingAmount", ignore = true)
    @Mapping(target = "date", ignore = true)

    // Ignore all calculated financials
    @Mapping(target = "sousTotalHT", ignore = true)
    @Mapping(target = "totalDiscountAmount", ignore = true)
    @Mapping(target = "amountHTAfterDiscount", ignore = true)
    @Mapping(target = "vat", ignore = true)
    @Mapping(target = "totalWithTax", ignore = true)

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "payments", ignore = true)
    Commande toEntity(CommandeDto dto);
}