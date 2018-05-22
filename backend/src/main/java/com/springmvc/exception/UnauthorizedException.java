package com.springmvc.exception;

public class UnauthorizedException extends StatusException {

    public UnauthorizedException(String message) {
        super(message, 401);
    }
}
