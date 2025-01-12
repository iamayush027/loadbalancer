package com.demo.loadbalancer.service;

import com.demo.loadbalancer.exceptions.DuplicateServerExistException;
import com.demo.loadbalancer.exceptions.ResourceNotFoundException;
import com.demo.loadbalancer.model.BackendServer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BackendServersService {
    void add(BackendServer backendServer) throws DuplicateServerExistException;

    List<BackendServer> findAll();
    List<BackendServer> findAllActiveServers();

    void toggleServer(String serverUrl, boolean activeFlag) throws ResourceNotFoundException;
    void remove(String serverUrl) throws ResourceNotFoundException;
}
