package com.last.call.itemservice.config;

import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DependencyHealthCheck {

    @Value("${SCHEDULER_SERVICE_URL:http://localhost:8084}")
    private String schedulerServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void checkDependencies() {
        waitForService(schedulerServiceUrl + "/health", "Scheduler Service");
    }

    private void waitForService(String url, String serviceName) {
        while (true) {
            try {
                restTemplate.getForObject(url, String.class);
                System.out.println(serviceName + " is ready");
                break;
            } catch (Exception e) {
                System.out.println("Waiting for " + serviceName + "...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
