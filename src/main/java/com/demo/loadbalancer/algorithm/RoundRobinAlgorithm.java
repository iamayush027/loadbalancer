package com.demo.loadbalancer.algorithm;

import com.demo.loadbalancer.exceptions.ServerNotAvailableException;
import com.demo.loadbalancer.model.BackendServer;
import com.demo.loadbalancer.model.Constant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service(value = Constant.ROUND_ROBIN)
public class RoundRobinAlgorithm implements LoadBalancingAlgorithm {
    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public BackendServer selectServer(List<BackendServer> servers) {

        if (servers.isEmpty()) {
            throw new ServerNotAvailableException("Server not available for routing");
        }
        int serverIndex = index.getAndUpdate(i -> (i + 1) % servers.size());
        return servers.get(serverIndex);
    }

    @Override
    public AlgorithmType getType() {
        return AlgorithmType.ROUND_ROBIN;
    }
}
