package com.demo.loadbalancer.model;

public class BackendServer {
    private final String url;
    private boolean isHealthy;
    private boolean isActive;

    public BackendServer(String url) {
        this.url = url;
        this.isHealthy = true;
        this.isActive=true;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getUrl() {
        return url;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public void setHealthy(boolean healthy) {
        this.isHealthy = healthy;
    }

    @Override
    public String toString() {
        return "BackendServer{" +
                "url='" + url + '\'' +
                ", isHealthy=" + isHealthy +
                ", isActive=" + isActive +
                '}';
    }
}
