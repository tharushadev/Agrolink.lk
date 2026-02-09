package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration; // ✅ CRITICAL IMPORT
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class }) // ✅ DISABLES THE LOGIN WALL
@ComponentScan(basePackages = {"com.example.demo", "com.agrolink"})
@EnableMongoRepositories(basePackages = {
		"com.example.demo",
		"com.agrolink.repository"
})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}