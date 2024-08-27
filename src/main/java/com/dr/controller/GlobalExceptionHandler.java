package com.dr.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public String handleSQLException(SQLException e){
        System.out.println(e.getMessage());
        return "An error occurred in DB while processing your request. Please try again later.";
    }

    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointerException(NullPointerException e){
        System.out.println(e.getMessage());
        return "An error occurred while performing operation with null value.";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e){
        System.out.println(e.getMessage());
        return "An unexpected error occurred. Please try again later.";
    }
}
