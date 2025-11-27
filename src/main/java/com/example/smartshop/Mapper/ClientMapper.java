package com.example.smartshop.Mapper;


import com.example.smartshop.DTO.ClientCreationDto;
import com.example.smartshop.DTO.ClientDto;
import com.example.smartshop.Models.Client;
import com.example.smartshop.Models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toClientEntity(ClientCreationDto dto);
    User toUserEntity(ClientCreationDto dto);
    @Mapping(source = "user.id", target = "userId")
    ClientDto toDto(Client client);
}
