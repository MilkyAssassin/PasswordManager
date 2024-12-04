package main.java.com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {
    "com.example.backend",
    "com.example.backend.config",
    "com.example.backend.controller"
})

public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
