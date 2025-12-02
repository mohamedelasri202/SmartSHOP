package com.example.smartshop.Controllers;


import com.example.smartshop.DTO.CommandeDto;
import com.example.smartshop.Exceptions.ForbiddenAccessException;
import com.example.smartshop.Models.Enums.UserRole;
import com.example.smartshop.Util.AuthUtil;
import com.example.smartshop.Services.CommandeServiceInterface;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class CommandeController {

    private final CommandeServiceInterface commandeService;
    private final AuthUtil authUtil;

    public  CommandeController(CommandeServiceInterface commandeService, AuthUtil authUtil) {
        this.commandeService = commandeService;
        this.authUtil = authUtil;
    }


    @PostMapping ("createOrder")
    public ResponseEntity<CommandeDto> createOrder(@RequestBody CommandeDto orderDto, HttpSession session) {


        if (!authUtil.isAdmin(session)) {

            throw new ForbiddenAccessException("Only ADMIN users are authorized to create new orders.");
        }

        CommandeDto savedOrderDto = commandeService.createCommande(orderDto);

        return new ResponseEntity<>(savedOrderDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<CommandeDto> confirmOrder(@PathVariable Long id, HttpSession session) {



        if (!authUtil.isAdmin(session)) {

            throw new ForbiddenAccessException("Only ADMIN users are authorized to create new orders.");
        }
        CommandeDto confirmedOrderDto = commandeService.confirmCommande(id);

        return new ResponseEntity<>(confirmedOrderDto, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<CommandeDto>> getOrderHistory(HttpSession session) {
        Long sessionUserId = authUtil.getLoggedInUserId(session);

        List<CommandeDto> orders;


        if (authUtil.isAdmin(session)) {
            orders = commandeService.getAllOrders();
        } else {
            orders = commandeService.getOrdersByClient(sessionUserId);
        }

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}