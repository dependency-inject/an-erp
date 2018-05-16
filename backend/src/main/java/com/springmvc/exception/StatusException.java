package com.springmvc.exception;

public abstract class StatusException extends Exception {

    private int status;

    public StatusException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
