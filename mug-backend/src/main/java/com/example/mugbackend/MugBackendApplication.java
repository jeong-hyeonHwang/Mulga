package com.example.mugbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MugBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MugBackendApplication.class, args);
	}

}
