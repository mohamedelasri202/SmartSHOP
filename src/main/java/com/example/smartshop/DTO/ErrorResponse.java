package com.example.smartshop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ErrorResponse {

    private final Date timestamp = new Date();

    private int httpStatus;
    private String errorType;
    private String message;
    private String path;


    public ErrorResponse(int httpStatus, String errorType, String message, String path) {

        this.httpStatus = httpStatus;
        this.errorType = errorType;
        this.message = message;
        this.path = path;
    }
}