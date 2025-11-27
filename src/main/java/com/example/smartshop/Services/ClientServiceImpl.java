package com.example.smartshop.Services;


import com.example.smartshop.DTO.ClientCreationDto;
import com.example.smartshop.DTO.ClientDto;
import com.example.smartshop.Exceptions.BusinessValidationException;
import com.example.smartshop.Mapper.ClientMapper;
import com.example.smartshop.Models.Client;
import com.example.smartshop.Models.Enums.LoyaltyLevel;
import com.example.smartshop.Models.Enums.UserRole;
import com.example.smartshop.Models.User;
import com.example.smartshop.Repositories.ClientRepository;
import com.example.smartshop.Repositories.UserRepository;
import com.example.smartshop.Util.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ClientServiceImpl implements ClientServiceInterface {

    UserRepository userRepository;
    ClientMapper clientMapper;
    ClientRepository clientRepository;
    PasswordEncoder passwordEncoder;

    public ClientServiceImpl(UserRepository userRepository, ClientMapper clientMapper, ClientRepository clientRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clientMapper =clientMapper;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ClientDto createClient(ClientCreationDto dto) {
        if(userRepository.findByUsername(dto.getUsername()).isPresent()){
            throw  new BusinessValidationException("Please Chose Another name");
        }
        User userToSave = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(UserRole.CLIENT)
                .build();
        User savedUser = userRepository.save(userToSave);

        Client clientToSave = clientMapper.toClientEntity(dto);
        clientToSave.setUser(savedUser);
        clientToSave.setLoyaltyLevel(LoyaltyLevel.BASIC);
        clientToSave.setTotalOrders(0);
        clientToSave.setTotalSpent(BigDecimal.ZERO);
        Client savedClient = clientRepository.save(clientToSave);
        return clientMapper.toDto(savedClient);


    }

    @Transactional
    public ClientDto updateClient(ClientDto clientDto,Long id) {
        Client client =clientRepository.findById(id).orElseThrow(()->new BusinessValidationException("Client with id: "+id+" not found"));
        clientMapper.updateClientFromDto(clientDto,client);

        Client updatedClient = clientRepository.save(client);

        return clientMapper.toDto(updatedClient);
    }







}
