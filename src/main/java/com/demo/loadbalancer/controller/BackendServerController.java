package com.demo.loadbalancer.controller;

import com.demo.loadbalancer.exceptions.DuplicateServerExistException;
import com.demo.loadbalancer.exceptions.ResourceNotFoundException;
import com.demo.loadbalancer.model.BackendServer;
import com.demo.loadbalancer.service.BackendServersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend-server")
public class BackendServerController {
    @Autowired
    private BackendServersService backendServersService;

    @PostMapping("/{url}")
    public ResponseEntity addServer(@PathVariable(value = "url") String serverUrl) throws DuplicateServerExistException {
        backendServersService.add(new BackendServer(serverUrl));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Backend server registered: " + serverUrl);
    }

    @GetMapping("/")
    public ResponseEntity<List<BackendServer>> findAllServer() throws DuplicateServerExistException {
        ;
        return ResponseEntity.status(HttpStatus.OK)
                .body(backendServersService.findAll());
    }

    @DeleteMapping("/{url}")
    public ResponseEntity removeServer(@PathVariable("url") String serverUrl) throws ResourceNotFoundException {
        backendServersService.remove(serverUrl);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Removed server: " + serverUrl);
    }

    @PatchMapping("/disable/{url}")
    public ResponseEntity disableServer(@PathVariable("url") String serverUrl) throws ResourceNotFoundException {
        backendServersService.toggleServer(serverUrl, false);
        return ResponseEntity.status(HttpStatus.OK).body("Removed server: " + serverUrl);

    }

    @PatchMapping("/enable/{url}")
    public ResponseEntity enableServer(@PathVariable("url") String serverUrl) throws ResourceNotFoundException {
        backendServersService.toggleServer(serverUrl, true);
        return ResponseEntity.status(HttpStatus.OK).body("Removed server: " + serverUrl);

    }
}
