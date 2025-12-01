package com.example.smartshop.Controllers;

import com.example.smartshop.DTO.PaymentDto;
import com.example.smartshop.Exceptions.ForbiddenAccessException;
import com.example.smartshop.Services.PaymentServiceInterface;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.smartshop.Util.AuthUtil;

@RestController
@RequestMapping("/api/orders") // Map payments under the parent resource, 'orders'
public class PaymentController {

    private final PaymentServiceInterface paymentService;
    private final AuthUtil authUtil;

    public PaymentController(PaymentServiceInterface paymentService, AuthUtil authUtil) {
        this.paymentService = paymentService;
        this.authUtil = authUtil;
    }


    @PostMapping("/{commandeId}/payments")
    public ResponseEntity<PaymentDto> recordPayment(
            @PathVariable Long commandeId,
            @RequestBody PaymentDto paymentDto,
            HttpSession session) {
        if (!authUtil.isAdmin(session)) {
            throw new ForbiddenAccessException("Access denied. Only ADMIN users can record payments.");
        }
        PaymentDto savedPayment = paymentService.recordPayment(commandeId, paymentDto);
        return new ResponseEntity<>(savedPayment, HttpStatus.CREATED);
    }}
