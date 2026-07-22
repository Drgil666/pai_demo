package com.example.pai_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class PaiDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaiDemoApplication.class, args);
    }

}
