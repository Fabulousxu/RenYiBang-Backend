package com.renyibang.userapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.renyibang.feignclient")
public class UserApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApiApplication.class, args);
	}

}
