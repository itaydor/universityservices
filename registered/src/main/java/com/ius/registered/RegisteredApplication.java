package com.ius.registered;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(
        basePackages = "com.ius.clients"
)
public class RegisteredApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegisteredApplication.class, args);
    }
}
