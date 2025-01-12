package com.demo.loadbalancer.algorithm;

import com.demo.loadbalancer.exceptions.ServerNotAvailableException;
import com.demo.loadbalancer.model.BackendServer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomSelectionAlgorithmTest {

    @Test
    public void testSelectServer_fromSingleServerList() {
        // Arrange
        List<BackendServer> servers = Collections.singletonList(new BackendServer("server1"));
        RandomSelectionAlgorithm algorithm = new RandomSelectionAlgorithm();

        // Act
        BackendServer selectedServer = algorithm.selectServer(servers);

        // Assert
        assertNotNull(selectedServer);
        assertEquals("server1", selectedServer.getUrl());
    }

    @Test
    public void testSelectServer_fromEmptyList() {
        // Arrange
        List<BackendServer> servers = Collections.emptyList();
        RandomSelectionAlgorithm algorithm = new RandomSelectionAlgorithm();

        // Act & Assert
        assertThrows(ServerNotAvailableException.class, () -> {
            algorithm.selectServer(servers);
        });
    }

    @Test
    public void testSelectServer_fromNonEmptyList() {
        // Arrange
        List<BackendServer> servers = Arrays.asList(new BackendServer("server1"), new BackendServer("server2"), new BackendServer("server3"));
        RandomSelectionAlgorithm algorithm = new RandomSelectionAlgorithm();

        // Act
        BackendServer selectedServer1 = algorithm.selectServer(servers);
        BackendServer selectedServer2 = algorithm.selectServer(servers);

        // Assert
        assertNotNull(selectedServer1);
        assertNotNull(selectedServer2);
        assertTrue(servers.contains(selectedServer1));
        assertTrue(servers.contains(selectedServer2));

        // Ensure randomness by checking if two different selections occur.
        assertNotEquals(selectedServer1.getUrl(), selectedServer2.getUrl()); // This may fail due to randomness, but will likely pass over multiple runs.
    }

}