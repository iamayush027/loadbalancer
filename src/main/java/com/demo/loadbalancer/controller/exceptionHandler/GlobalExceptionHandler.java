package com.demo.loadbalancer.controller.exceptionHandler;

import com.demo.loadbalancer.exceptions.ApiRequestException;
import com.demo.loadbalancer.exceptions.DuplicateServerExistException;
import com.demo.loadbalancer.exceptions.ResourceNotFoundException;
import com.demo.loadbalancer.exceptions.ServerNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiRequestException.class)
    public Mono<ResponseEntity<String>> handleApiRequestException(ApiRequestException ex) {
        logger.debug("Handling ApiRequestException: {}", ex.getMessage());  // Debug-level logging for method entry and exception message
        logger.error("ApiRequestException occurred: ", ex);  // Error-level logging for full stack trace
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateServerExistException.class)
    public Mono<ResponseEntity<String>> handleDuplicateServerExistException(DuplicateServerExistException ex) {
        logger.debug("Handling DuplicateServerExistException: {}", ex.getMessage());  // Debug-level logging for method entry and exception message
        logger.error("DuplicateServerExistException occurred: ", ex);  // Error-level logging for full stack trace
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.debug("Handling ResourceNotFoundException: {}", ex.getMessage());  // Debug-level logging for method entry and exception message
        logger.error("ResourceNotFoundException occurred: ", ex);  // Error-level logging for full stack trace
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public Mono<ResponseEntity<String>> handleIOException(IOException ex) {
        logger.debug("Handling IOException: {}", ex.getMessage());  // Debug-level logging for method entry and exception message
        logger.error("IOException occurred: ", ex);  // Error-level logging for full stack trace
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleGenericException(Exception ex) {
        logger.debug("Handling generic Exception: {}", ex.getMessage());  // Debug-level logging for method entry and exception message
        logger.error("Generic Exception occurred: ", ex);  // Error-level logging for full stack trace
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage()));
    }

    @ExceptionHandler(ServerNotAvailableException.class)
    public Mono<ResponseEntity<String>> handleServerNotAvailableException(ServerNotAvailableException ex) {
        logger.debug("Handling ServerNotAvailableException Exception: {}", ex.getMessage());  // Debug-level logging for method entry and exception message
        logger.error("Generic ServerNotAvailableException occurred: ", ex);  // Error-level logging for full stack trace
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage()));
    }

}

