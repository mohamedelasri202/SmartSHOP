package com.example.smartshop.Exceptions;

public class BusinessValidationException extends RuntimeException{
    public BusinessValidationException(String message){
        super(message);
    }
}
