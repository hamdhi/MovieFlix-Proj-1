package com.myjwtai.jwtai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JwtaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtaiApplication.class, args);
	}

}
