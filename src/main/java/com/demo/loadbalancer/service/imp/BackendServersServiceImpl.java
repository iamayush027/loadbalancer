package com.demo.loadbalancer.service.imp;

import com.demo.loadbalancer.exceptions.DuplicateServerExistException;
import com.demo.loadbalancer.exceptions.ResourceNotFoundException;
import com.demo.loadbalancer.model.BackendServer;
import com.demo.loadbalancer.service.BackendServersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BackendServersServiceImpl implements BackendServersService {
    private static final String HEALTH_CHECK_PATH = "/actuator/health";  // Health check endpoint
    private static final Logger logger = LoggerFactory.getLogger(BackendServersServiceImpl.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private Map<String, BackendServer> backendServers = new HashMap();

    @Autowired
    public BackendServersServiceImpl(@Value("${backend.servers}") List<String> initialBackendServers) {
        for (String serverUrl : initialBackendServers) {
            if(!serverUrl.isBlank())
            this.backendServers.put(serverUrl, new BackendServer(serverUrl));
        }
    }

    /**
     * Add a new BackendServer to the directory of servers
     *
     * @param backendServer
     * @throws DuplicateServerExistException
     */
    @Override
    public void add(BackendServer backendServer) throws DuplicateServerExistException {
        if (!backendServers.containsKey(backendServer)) {
            backendServers.put(backendServer.getUrl(), backendServer);
        } else {
            throw new DuplicateServerExistException("BackendServer already Exist");
        }
    }

    @Override
    public List<BackendServer> findAll() {
        return backendServers.values().stream().toList();
    }

    private List<BackendServer> findAllUnHealthyServers() {
        return backendServers.values().stream().filter(backendServer -> !backendServer.isHealthy() && backendServer.isActive()).toList();
    }

    @Override
    public List<BackendServer> findAllActiveServers() {
        return backendServers.values().stream().filter(backendServer -> backendServer.isActive() && backendServer.isHealthy()).toList();
    }

    @Override
    public void toggleServer(String serverUrl, boolean activeFlag) throws ResourceNotFoundException {
        if (backendServers.containsKey(serverUrl)) {
            BackendServer server = backendServers.get(serverUrl);
            server.setActive(activeFlag);
            backendServers.put(serverUrl, server);
        } else {
            throw new ResourceNotFoundException("Requested URL is not registered, URL: " + serverUrl);
        }
    }

    @Override
    public void remove(String serverUrl) throws ResourceNotFoundException {
        if (backendServers.containsKey(serverUrl)) {
            backendServers.remove(serverUrl);
        } else {
            throw new ResourceNotFoundException("Requested URL is not registered, URL: " + serverUrl);
        }
    }

    @Scheduled(fixedRate = 10000)
    public void checkAllServersHealth() {
        for (BackendServer server : findAllActiveServers()) {
            checkServerHealth(server);
        }
    }

    @Scheduled(fixedRate = 5000)  // Check every 5 seconds for unhealthy servers
    public void checkUnhealthyServersHealth() {
        for (BackendServer server : findAllUnHealthyServers()) {
            checkServerHealth(server);
        }
    }

    private void checkServerHealth(BackendServer server) {
        String healthCheckUrl = server.getUrl() + HEALTH_CHECK_PATH;
        try {
            HttpStatusCode status = restTemplate.getForEntity(healthCheckUrl, String.class).getStatusCode();
            if (status.is2xxSuccessful()) {
                // If status is 2xx, mark the server as healthy
                server.setHealthy(true);
                logger.debug("Server " + server.getUrl() + " is healthy.");
            } else {
                // Otherwise, mark as unhealthy
                server.setHealthy(false);
                logger.error("Server " + server.getUrl() + " is unhealthy. Status: " + status);
            }
        } catch (RestClientException e) {
            server.setHealthy(false);
            logger.error("Server " + server.getUrl() + " failed health check. Error: " + e.getMessage());
        }
    }

}
