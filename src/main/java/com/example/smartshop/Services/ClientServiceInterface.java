package com.example.smartshop.Services;

import com.example.smartshop.DTO.ClientCreationDto;
import com.example.smartshop.DTO.ClientDto;

import java.util.List;

public interface ClientServiceInterface {

    ClientDto createClient(ClientCreationDto clientDto);
    ClientDto updateClient(ClientDto clientDto,Long id);
    ClientDto getClientProfile(Long id);
    List<ClientDto> getAllClients();
}
