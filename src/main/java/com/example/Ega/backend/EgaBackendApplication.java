package com.example.Ega.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class EgaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EgaBackendApplication.class, args);
    }
    
    @GetMapping("/")
    public String home() {
        return "EGA BANK Backend is running! Database: MySQL ega_bank";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK - Backend operational with MySQL database";
    }
}
