package com.demo.loadbalancer.exceptions;

public class ServerNotAvailableException extends RuntimeException{
    public ServerNotAvailableException(String message) {
        super(message);
    }

    public ServerNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
