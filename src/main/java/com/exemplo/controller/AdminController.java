package com.exemplo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @PostMapping("/shutdown")
    public String shutdown() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.exit(0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        return "Serviço será encerrado em breve.";
    }
    
    @GetMapping("/status")
    public String getStatus() {
        return "running";
    }
} 