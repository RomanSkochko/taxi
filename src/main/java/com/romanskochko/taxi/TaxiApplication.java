package com.romanskochko.taxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.romanskochko.taxi")
public class TaxiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaxiApplication.class, args);
    }
}
