package com.demo.loadbalancer.algorithm;

import com.demo.loadbalancer.exceptions.ServerNotAvailableException;
import com.demo.loadbalancer.model.BackendServer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoundRobinAlgorithmTest {
    @Test
    public void testSelectServer_roundRobinSelection() {
        // Arrange
        List<BackendServer> servers = Arrays.asList(new BackendServer("server1"), new BackendServer("server2"), new BackendServer("server3"));
        RoundRobinAlgorithm algorithm = new RoundRobinAlgorithm();

        // Act
        BackendServer selectedServer1 = algorithm.selectServer(servers);
        BackendServer selectedServer2 = algorithm.selectServer(servers);
        BackendServer selectedServer3 = algorithm.selectServer(servers);
        BackendServer selectedServer4 = algorithm.selectServer(servers); // Should loop back to server1

        // Assert
        assertEquals("server1", selectedServer1.getUrl());
        assertEquals("server2", selectedServer2.getUrl());
        assertEquals("server3", selectedServer3.getUrl());
        assertEquals("server1", selectedServer4.getUrl());  // Check if it loops back to the first server
    }

    @Test
    public void testSelectServer_fromEmptyList() {
        // Arrange
        List<BackendServer> servers = Collections.emptyList();
        RoundRobinAlgorithm algorithm = new RoundRobinAlgorithm();

        // Act & Assert
        assertThrows(ServerNotAvailableException.class, () -> {
            algorithm.selectServer(servers);
        });
    }

    @Test
    public void testSelectServer_fromSingleServerList() {
        // Arrange
        List<BackendServer> servers = Collections.singletonList(new BackendServer("server1"));
        RoundRobinAlgorithm algorithm = new RoundRobinAlgorithm();

        // Act
        BackendServer selectedServer1 = algorithm.selectServer(servers);
        BackendServer selectedServer2 = algorithm.selectServer(servers); // Should return server1 again

        // Assert
        assertNotNull(selectedServer1);
        assertEquals("server1", selectedServer1.getUrl());
        assertEquals("server1", selectedServer2.getUrl());  // Should always return the same server
    }

    @Test
    public void testRoundRobin_multipleSelections() {
        // Arrange
        List<BackendServer> servers = Arrays.asList(new BackendServer("server1"), new BackendServer("server2"), new BackendServer("server3"));
        RoundRobinAlgorithm algorithm = new RoundRobinAlgorithm();
        List<BackendServer> selectedServers = new ArrayList<>();

        // Act
        for (int i = 0; i < 10; i++) {
            selectedServers.add(algorithm.selectServer(servers));
        }

        // Assert: Check the order of selections follows round-robin
        assertEquals("server1", selectedServers.get(0).getUrl());
        assertEquals("server2", selectedServers.get(1).getUrl());
        assertEquals("server3", selectedServers.get(2).getUrl());
        assertEquals("server1", selectedServers.get(3).getUrl());  // Loop back
        assertEquals("server2", selectedServers.get(4).getUrl());
        assertEquals("server3", selectedServers.get(5).getUrl());
        assertEquals("server1", selectedServers.get(6).getUrl());
        assertEquals("server2", selectedServers.get(7).getUrl());
        assertEquals("server3", selectedServers.get(8).getUrl());
        assertEquals("server1", selectedServers.get(9).getUrl());  // Check the 10th selection
    }


}