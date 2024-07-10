package com.renyibang.serviceapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.renyibang.global.client")
public class ServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApiApplication.class, args);
	}

}
