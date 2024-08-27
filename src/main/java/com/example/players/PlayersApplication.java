package com.example.players;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PlayersApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();

		// Only set environment variables if they are not already set
		System.setProperty("POSTGRES_USER", System.getenv().getOrDefault("POSTGRES_USER", dotenv.get("POSTGRES_USER")));
		System.setProperty("POSTGRES_PASSWORD", System.getenv().getOrDefault("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD")));
		System.setProperty("POSTGRES_DB", System.getenv().getOrDefault("POSTGRES_DB", dotenv.get("POSTGRES_DB")));
		System.setProperty("SPRING_DATASOURCE_USERNAME", System.getenv().getOrDefault("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME")));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", System.getenv().getOrDefault("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD")));
		System.setProperty("SPRING_JPA_HIBERNATE_DDL_AUTO", System.getenv().getOrDefault("SPRING_JPA_HIBERNATE_DDL_AUTO", dotenv.get("SPRING_JPA_HIBERNATE_DDL_AUTO")));
		System.setProperty("SPRING_DATASOURCE_URL", System.getenv().getOrDefault("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL")));


		SpringApplication.run(PlayersApplication.class, args);
	}
}
