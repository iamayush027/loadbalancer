package com.demo.loadbalancer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        //TODO: For any configuration we need to add here, eg. Authorization.
        return WebClient.builder();
    }
}
