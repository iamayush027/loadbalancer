package com.demo.loadbalancer.algorithm;

import com.demo.loadbalancer.model.BackendServer;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface LoadBalancingAlgorithm {
    BackendServer selectServer(List<BackendServer> servers);
    AlgorithmType getType();

}
