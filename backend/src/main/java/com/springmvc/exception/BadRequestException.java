package com.springmvc.exception;

public class BadRequestException extends StatusException {

    public BadRequestException(String message) {
        super(message, 400);
    }
}
