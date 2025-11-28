package com.example.smartshop.Mapper;

import com.example.smartshop.DTO.PaymentDto;
import com.example.smartshop.Models.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "commande", ignore = true)
    @Mapping(target = "status", ignore = true)
    Payment toEntity(PaymentDto dto);


    @Mapping(source = "commande.id", target = "commandeId")
    PaymentDto toDto(Payment entity);


}