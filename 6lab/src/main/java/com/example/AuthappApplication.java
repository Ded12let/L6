package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.net.URI;

@SpringBootApplication
public class AuthappApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthappApplication.class, args);

        // Автоматически открыть браузер
        String url = "http://localhost:8080/login";
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                System.err.println("Не удалось открыть браузер");
                e.printStackTrace();
            }
        } else {
            System.out.println("Откройте вручную: " + url);
        }
    }
}