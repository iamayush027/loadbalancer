package com.demo.loadbalancer.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class LoadBalancerFactory {
    private final Map<String, LoadBalancingAlgorithm> loadBalancingAlgorithmMap;

    @Autowired
    public LoadBalancerFactory(Map<String, LoadBalancingAlgorithm> loadBalancingAlgorithmMap) {
        this.loadBalancingAlgorithmMap = loadBalancingAlgorithmMap;

    }

    public LoadBalancingAlgorithm getAlgorithm(String algorithmType) {
        log.debug("Fetched Algorithm implementation for: " + algorithmType);
        return loadBalancingAlgorithmMap.get(algorithmType);
    }
}
