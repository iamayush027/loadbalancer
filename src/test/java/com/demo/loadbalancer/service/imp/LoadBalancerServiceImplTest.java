package com.demo.loadbalancer.service.imp;

import com.demo.loadbalancer.algorithm.LoadBalancingAlgorithm;
import com.demo.loadbalancer.model.BackendServer;
import com.demo.loadbalancer.service.BackendServersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoadBalancerServiceImplTest {

    @Mock
    private LoadBalancingAlgorithm loadBalancingAlgorithm;

    @Mock
    private BackendServersService backendServersService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private ServerWebExchange serverWebExchange;

    @InjectMocks
    private LoadBalancerServiceImpl loadBalancerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
    }

    @Test
    public void testRedirectRequest_withActiveBackendServers() throws Exception {
        // Arrange
        BackendServer server1 = new BackendServer("server1");
        BackendServer server2 = new BackendServer("server2");
        List<BackendServer> backendServers = Arrays.asList(server1, server2);

        when(backendServersService.findAllActiveServers()).thenReturn(backendServers);
        when(loadBalancingAlgorithm.selectServer(backendServers)).thenReturn(server1);

        // Mock request data
        HttpHeaders headers = new HttpHeaders();
        headers.add("Header", "value");
        HttpMethod method = HttpMethod.GET;
        URI uri = new URI("/test");
        when(serverWebExchange.getRequest().getHeaders()).thenReturn(headers);
        when(serverWebExchange.getRequest().getMethod()).thenReturn(method);
        when(serverWebExchange.getRequest().getURI()).thenReturn(uri);

        // Mock WebClient response
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Response from server", HttpStatus.OK);
        when(requestBodyUriSpec.uri(any(URI.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.exchangeToMono(any())).thenReturn(Mono.just(responseEntity));

        // Act
        Mono<ResponseEntity<String>> result = loadBalancerService.redirectRequest(serverWebExchange);

        // Assert
        ResponseEntity<String> response = result.block();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Response from server", response.getBody());
    }

    @Test
    public void testRedirectRequest_withEmptyBackendServers() throws Exception {
        // Arrange
        List<BackendServer> backendServers = Collections.emptyList();
        when(backendServersService.findAllActiveServers()).thenReturn(backendServers);

        // Mock request data
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.GET;
        URI uri = new URI("/test");
        when(serverWebExchange.getRequest().getHeaders()).thenReturn(headers);
        when(serverWebExchange.getRequest().getMethod()).thenReturn(method);
        when(serverWebExchange.getRequest().getURI()).thenReturn(uri);

        // Act
        Mono<ResponseEntity<String>> result = loadBalancerService.redirectRequest(serverWebExchange);

        // Assert
        ResponseEntity<String> response = result.block();
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Forwarding Error"));
    }

    @Test
    public void testRedirectRequest_withErrorFromBackend() throws Exception {
        // Arrange
        BackendServer server1 = new BackendServer("server1");
        List<BackendServer> backendServers = Arrays.asList(server1);

        when(backendServersService.findAllActiveServers()).thenReturn(backendServers);
        when(loadBalancingAlgorithm.selectServer(backendServers)).thenReturn(server1);

        // Mock request data
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.GET;
        URI uri = new URI("/test");
        when(serverWebExchange.getRequest().getHeaders()).thenReturn(headers);
        when(serverWebExchange.getRequest().getMethod()).thenReturn(method);
        when(serverWebExchange.getRequest().getURI()).thenReturn(uri);

        // Mock WebClient response for error
        ResponseEntity<String> errorResponse = new ResponseEntity<>("Error from backend", HttpStatus.INTERNAL_SERVER_ERROR);
        when(requestBodyUriSpec.uri(any(URI.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.exchangeToMono(any())).thenReturn(Mono.just(errorResponse));

        // Act
        Mono<ResponseEntity<String>> result = loadBalancerService.redirectRequest(serverWebExchange);

        // Assert
        ResponseEntity<String> response = result.block();
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error from backend", response.getBody());
    }

    @Test
    public void testRedirectRequest_withServerError() throws Exception {
        // Arrange
        BackendServer server1 = new BackendServer("server1");
        List<BackendServer> backendServers = Arrays.asList(server1);

        when(backendServersService.findAllActiveServers()).thenReturn(backendServers);
        when(loadBalancingAlgorithm.selectServer(backendServers)).thenReturn(server1);

        // Mock request data
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.GET;
        URI uri = new URI("/test");
        when(serverWebExchange.getRequest().getHeaders()).thenReturn(headers);
        when(serverWebExchange.getRequest().getMethod()).thenReturn(method);
        when(serverWebExchange.getRequest().getURI()).thenReturn(uri);

        // Mock WebClient to throw an error
        when(requestBodyUriSpec.uri(any(URI.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.exchangeToMono(any())).thenReturn(Mono.error(new RuntimeException("Internal server error")));

        // Act
        Mono<ResponseEntity<String>> result = loadBalancerService.redirectRequest(serverWebExchange);

        // Assert
        ResponseEntity<String> response = result.block();
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Forwarding Error"));
    }
}
