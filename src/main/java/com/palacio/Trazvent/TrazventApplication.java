package com.palacio.Trazvent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrazventApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrazventApplication.class, args);
	}

}
