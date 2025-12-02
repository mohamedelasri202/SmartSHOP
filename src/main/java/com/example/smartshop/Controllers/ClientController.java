package com.example.smartshop.Controllers;

import com.example.smartshop.DTO.ClientCreationDto;
import com.example.smartshop.DTO.ClientDto;
import com.example.smartshop.Exceptions.ForbiddenAccessException;
import com.example.smartshop.Models.Client;
import com.example.smartshop.Models.Enums.UserRole;
import com.example.smartshop.Repositories.ClientRepository;
import com.example.smartshop.Services.ClientServiceInterface;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.smartshop.Util.AuthUtil;

import java.util.List;


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

    @PutMapping("/{id}/updateClient")
    public ResponseEntity<ClientDto> updateClient(@PathVariable("id") long id, @RequestBody ClientDto dto, HttpSession session) {
        if (!authUtil.isAdmin(session)) {
            throw new ForbiddenAccessException("Sorry you don't have permission to access this resource");
        }


        ClientDto updatedClientDto = clientService.updateClient(dto, id);

        return new ResponseEntity<>(updatedClientDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientProfile(@PathVariable Long id, HttpSession session) {

        Long sessionUserId = authUtil.getLoggedInUserId(session);
        UserRole userRole = (UserRole) session.getAttribute("userRole");


        if (!"ADMIN".equals(userRole) && !id.equals(sessionUserId)) {

            throw new ForbiddenAccessException("Access denied. You can only view your own profile.");
        }


        ClientDto clientDto = clientService.getClientProfile(id);
        return new ResponseEntity<>(clientDto, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients(HttpSession session) {


        if (!authUtil.isAdmin(session)) {
            throw new ForbiddenAccessException("Access denied. Only ADMIN can view all client data.");
        }


        List<ClientDto> allClients = clientService.getAllClients();


        return new ResponseEntity<>(allClients, HttpStatus.OK);
    }


}
