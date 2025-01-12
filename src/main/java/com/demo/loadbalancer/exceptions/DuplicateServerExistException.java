package com.demo.loadbalancer.exceptions;

public class DuplicateServerExistException extends Exception{
    public DuplicateServerExistException(String message) {
        super(message);
    }

    public DuplicateServerExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
