package com.example.smartshop.Mapper;

import com.example.smartshop.DTO.CommandeDto;

import com.example.smartshop.Models.Commande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class})
public interface CommandeMapper {


    @Mapping(source = "client.id", target = "clientId")


    @Mapping(source = "items", target = "items")
    @Mapping(source = "payments", target = "payments")

    CommandeDto toDto(Commande commande);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "remainingAmount", ignore = true)
    @Mapping(target = "subtotalHT", ignore = true)

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "payments", ignore = true)
    Commande toEntity(CommandeDto dto);
}