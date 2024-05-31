package com.example.demo.service.serviceImpl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceTask {

    @Scheduled(fixedRate = 10000) // 1 day in milliseconds; 1000ms=1s
    public void performMaintenance() {
        System.out.println("bao tri");
    }
}
