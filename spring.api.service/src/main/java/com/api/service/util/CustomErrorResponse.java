package com.api.service.util;

import org.springframework.http.HttpStatus;


public class CustomErrorResponse {

    private HttpStatus status;
    private String error;
    private String message;


    private CustomErrorResponse() {
    }


    public CustomErrorResponse(String message) {
        this.status = status;
    }
    public CustomErrorResponse(HttpStatus status, Throwable ex) {
        this.status = status;
        this.error = "Internal Server Error";
        this.message = "Unexpected error during the process";
    }

    public CustomErrorResponse(String error, String message, HttpStatus status) {
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
