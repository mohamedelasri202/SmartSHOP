package com.example.smartshop.Mapper;


import com.example.smartshop.DTO.ClientCreationDto;
import com.example.smartshop.DTO.ClientDto;
import com.example.smartshop.Models.Client;
import com.example.smartshop.Models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toClientEntity(ClientCreationDto dto);
    User toUserEntity(ClientCreationDto dto);
    @Mapping(source = "user.id", target = "userId")
    ClientDto toDto(Client client);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "loyaltyLevel", ignore = true)
    @Mapping(target = "totalOrders", ignore = true)
    @Mapping(target = "totalSpent", ignore = true)
    @Mapping(target = "firstOrderDate", ignore = true)
    @Mapping(target = "lastOrderDate", ignore = true)
    void updateClientFromDto(ClientDto dto, @MappingTarget Client entity);
}
