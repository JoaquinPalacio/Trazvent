package com.palacio.Trazvent;

import org.springframework.boot.SpringApplication;

public class TestTrazventApplication {

	public static void main(String[] args) {
		SpringApplication.from(TrazventApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
