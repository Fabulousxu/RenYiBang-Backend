package com.renyibang.taskapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.renyibang.feignclient")
public class TaskApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApiApplication.class, args);
	}

}
