package com.example.demo;

import com.agrolink.config.DemoUsersProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration; // ✅ CRITICAL IMPORT
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(
		exclude = { SecurityAutoConfiguration.class },
		scanBasePackages = { "com.agrolink" }
) // ✅ DISABLES THE LOGIN WALL + scans only the active API package
@EnableMongoRepositories(basePackages = {
		"com.agrolink.repository"
})
@EnableConfigurationProperties(DemoUsersProperties.class)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}