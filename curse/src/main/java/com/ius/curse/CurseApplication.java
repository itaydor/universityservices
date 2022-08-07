package com.ius.curse;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
public class CurseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurseApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CurseRepository curseRepository){
        List<String> cursesNames = Arrays.asList("Hedva-1", "Hedva-2", "Algebra-1", "Algebra-2",
                "Logic", "Java-basic", "OOP");
        return args -> {
            cursesNames.forEach(curseName -> {
                curseRepository.save(
                        Curse.builder()
                                .name(curseName)
                                .build()
                );
            });
        };
    }

}
