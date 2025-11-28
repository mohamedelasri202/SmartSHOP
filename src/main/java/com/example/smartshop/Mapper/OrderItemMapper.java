package com.example.smartshop.Mapper;

import com.example.smartshop.DTO.OrderItemDto;
import com.example.smartshop.DTO.CommandeDto;
import com.example.smartshop.Models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {


    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemDto toDto(OrderItem entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "lineTotal", ignore = true)
    @Mapping(target = "commande", ignore = true)
    OrderItem toEntity(OrderItemDto dto);
}