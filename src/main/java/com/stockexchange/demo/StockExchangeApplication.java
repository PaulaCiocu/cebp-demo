package com.stockexchange.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.awt.*;
import java.net.URI;

@SpringBootApplication(scanBasePackages = "com.stockexchange.demo")
@EnableJpaRepositories(basePackages = "com.stockexchange.demo.repository")
@EntityScan(basePackages = "com.stockexchange.demo.entity")
public class StockExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockExchangeApplication.class, args);

		//ToDo: Uncomment this if you want to run locally. Lines commented for EC2 Instance JAR
		/*
		try {
			String url = "http://localhost:8000/swagger-ui/index.html";
			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("win")) {
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			} else if (os.contains("mac")) {
				Runtime.getRuntime().exec("open " + url);
			} else if (os.contains("nix") || os.contains("nux")) {
				Runtime.getRuntime().exec("xdg-open " + url);
			} else {
				System.err.println("Unsupported OS, cannot open browser automatically.");
			}
		} catch (Exception e) {
			System.err.println("Failed to open Swagger UI: " + e.getMessage());
		}
		 */
	}

}
