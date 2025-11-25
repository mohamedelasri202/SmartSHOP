package com.example.smartshop.Config;

import com.example.smartshop.Exceptions.AuthenticationFailedException;
import com.example.smartshop.Exceptions.BusinessValidationException;
import com.example.smartshop.DTO.ErrorResponse; // Your Custom DTO
import com.example.smartshop.Exceptions.ResourceNotFoundException;
import org.hibernate.ResourceClosedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;



@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponse> businessValidationExceptionHandler(BusinessValidationException ex, WebRequest request) {


        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "BUSINESS_RULE_VIOLATION",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse>ResourceNotFoundExceptionHandler(ResourceClosedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resouces_NOT_FOUND",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<String>AuthenticationFailedExceptionHandler(AuthenticationFailedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "AUHTHENTICATION_FAILED",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
                return new ResponseEntity<>(errorResponse.toString(), HttpStatus.UNAUTHORIZED);


    }
}