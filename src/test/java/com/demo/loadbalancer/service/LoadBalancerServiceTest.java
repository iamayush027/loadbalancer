//package com.demo.loadbalancer.service;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class LoadBalancerServiceTests {
//
//    private LoadBalancerService loadBalancerService;
//
////    @BeforeEach
////    void setUp() {
////        loadBalancerService = new LoadBalancerService("round-robin");
////        loadBalancerService.addBackendServer(new BackendServer("localhost", 8081));
////        loadBalancerService.addBackendServer(new BackendServer("localhost", 8082));
////    }
//
//    @Test
//    void testRoundRobinAlgorithm() {
//        assertNotNull(loadBalancerService.selectServer());
//
//    }
//}