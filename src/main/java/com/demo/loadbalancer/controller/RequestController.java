package com.demo.loadbalancer.controller;


import com.demo.loadbalancer.aspect.LogExecutionTime;
import com.demo.loadbalancer.exceptions.ApiRequestException;
import com.demo.loadbalancer.service.LoadBalancerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("")
public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);
    @Autowired
    private LoadBalancerService loadBalancerService;

    @LogExecutionTime
    @RequestMapping(value = "/**", method = {GET, POST, PUT, DELETE, PATCH})
    public Mono<ResponseEntity<String>> redirectApiRequest(ServerWebExchange exchange) throws IOException {
        logger.debug("Received {} exchange for URI: {}", exchange.getRequest().getMethod(), exchange.getRequest().getMethod());
        return loadBalancerService.redirectRequest(exchange);

    }

}

