package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("org.example.model.repository")
public class FootballApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(FootballApiApplication.class, args);
	}
}
