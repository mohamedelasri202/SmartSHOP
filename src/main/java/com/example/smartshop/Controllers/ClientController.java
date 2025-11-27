package com.example.smartshop.Controllers;

import com.example.smartshop.DTO.ClientCreationDto;
import com.example.smartshop.DTO.ClientDto;
import com.example.smartshop.Exceptions.ForbiddenAccessException;
import com.example.smartshop.Models.Client;
import com.example.smartshop.Repositories.ClientRepository;
import com.example.smartshop.Services.ClientServiceInterface;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.smartshop.Util.AuthUtil;


@RestController
    @RequestMapping("api/client")
public class ClientController {

    private final ClientServiceInterface clientService;
    private final AuthUtil authUtil;
    public ClientController(ClientServiceInterface clientService, AuthUtil authUtil) {
        this.clientService = clientService;
        this.authUtil = authUtil;
    }


    @PostMapping("/creatClient")
    public ResponseEntity<ClientDto> addClient(@RequestBody ClientCreationDto dto , HttpSession session) {

        if(!authUtil.isAdmin(session)) {
             throw  new ForbiddenAccessException("Sorry you don't have permission to access this resource");

        }
        ClientDto createClient = clientService.createClient(dto);
        return  new ResponseEntity<>(createClient, HttpStatus.CREATED);

    }


}
