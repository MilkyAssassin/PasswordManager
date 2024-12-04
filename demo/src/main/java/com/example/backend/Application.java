
package main.java.com.example.backend;  // Remove main.java prefix

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "main.java.com.example.backend", exclude = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class}
    )
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}