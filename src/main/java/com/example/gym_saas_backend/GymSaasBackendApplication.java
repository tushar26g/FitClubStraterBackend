package com.example.gym_saas_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GymSaasBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymSaasBackendApplication.class, args);
	}

}
