package com.demo.loadbalancer.service.imp;

import com.demo.loadbalancer.algorithm.LoadBalancerFactory;
import com.demo.loadbalancer.algorithm.LoadBalancingAlgorithm;
import com.demo.loadbalancer.model.BackendServer;
import com.demo.loadbalancer.service.BackendServersService;
import com.demo.loadbalancer.service.LoadBalancerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Service
public class LoadBalancerServiceImpl implements LoadBalancerService {
    private static final Logger logger = LoggerFactory.getLogger(LoadBalancerServiceImpl.class);
    private final LoadBalancingAlgorithm loadBalancingAlgorithm;
    private final BackendServersService backendServersService;
    private final WebClient.Builder webClientBuilder;


    @Autowired
    public LoadBalancerServiceImpl(LoadBalancerFactory balancerFactory, BackendServersService backendServersService, @Value("${loadbalancer.algorithm:ROUND_ROBIN}") String algorithmType, WebClient.Builder builder) {
        loadBalancingAlgorithm = balancerFactory.getAlgorithm(algorithmType);
        this.backendServersService = backendServersService;
        this.webClientBuilder = builder;
    }

    /**
     *  Redirects the request to the backendServer
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Override
    public Mono<ResponseEntity<String>> redirectRequest(ServerWebExchange request) throws IOException {
        List<BackendServer> backendServersList = backendServersService.findAllActiveServers();
        BackendServer selectedServer = loadBalancingAlgorithm.selectServer(backendServersList);
        String requestUri = request.getRequest().getURI().getPath();
        HttpMethod requestMethod = request.getRequest().getMethod();
        HttpHeaders requestHeaders = request.getRequest().getHeaders();
        Mono<String> bodyMono = request.getRequest().getBody().collectList()
                .map(buffers -> buffers.stream()
                        .map(buffer -> buffer.toString(Charset.defaultCharset()))
                        .reduce(String::concat)
                        .orElse(""));
        WebClient.RequestBodySpec requestSpec = webClientBuilder.baseUrl(selectedServer.getUrl()).build().method(requestMethod).uri(requestUri) // Keep the same URI as the original request
                .headers(httpHeaders -> httpHeaders.addAll(requestHeaders)); // Forward original headers

        if (requestMethod == HttpMethod.POST || requestMethod == HttpMethod.PUT || requestMethod == HttpMethod.PATCH) {
            requestSpec.body(BodyInserters.fromPublisher(bodyMono, String.class));
        }

        return requestSpec.exchangeToMono(clientResponse -> {
            HttpStatusCode statusCode = clientResponse.statusCode();
            HttpHeaders responseHeaders = clientResponse.headers().asHttpHeaders();

            if (statusCode.isError()) {
                // Forward the error response as is
                return clientResponse.bodyToMono(String.class)
                        .map(errorBody -> ResponseEntity.status(statusCode).headers(responseHeaders).body(errorBody));
            } else {
                // Forward the successful response
                return clientResponse.bodyToMono(String.class)
                        .map(body -> ResponseEntity.status(statusCode).headers(responseHeaders).body(body));
            }
        }).onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Forwarding Error: " + e.getMessage())));

    }

}

