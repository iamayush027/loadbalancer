package com.demo.loadbalancer.service;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public interface LoadBalancerService {

    Mono<ResponseEntity<String>> redirectRequest(ServerWebExchange request) throws IOException;

}

