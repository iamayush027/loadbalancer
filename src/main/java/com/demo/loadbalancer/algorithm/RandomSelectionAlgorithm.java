package com.demo.loadbalancer.algorithm;


import com.demo.loadbalancer.exceptions.ServerNotAvailableException;
import com.demo.loadbalancer.model.BackendServer;
import com.demo.loadbalancer.model.Constant;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
@Service(value = Constant.RANDOM)
public class RandomSelectionAlgorithm implements LoadBalancingAlgorithm {

    private final Random random = new Random();

    @Override
    public BackendServer selectServer(List<BackendServer> servers) {
        if (servers.isEmpty()) {
            throw new  ServerNotAvailableException("Server not available for routing");
        }
        return servers.get(random.nextInt(servers.size()));
    }

    @Override
    public AlgorithmType getType() {
        return AlgorithmType.RANDOM;
    }
}

