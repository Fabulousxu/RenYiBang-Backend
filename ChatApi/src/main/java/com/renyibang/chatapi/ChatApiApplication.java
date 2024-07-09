package com.renyibang.chatapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.renyibang.feignclient")
public class ChatApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChatApiApplication.class, args);
  }
}
