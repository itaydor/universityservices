package com.ius.student;

import com.ius.student.student.Student;
import com.ius.student.student.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication(
        scanBasePackages = {
                "com.ius.student",
                "com.ius.kafka"
        }
)
@EnableEurekaClient
@EnableFeignClients(
        basePackages = "com.ius.clients"
)
public class StudentApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentApplication.class, args);
    }

//    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository){
        List<Student> students = Arrays.asList(
                Student.builder()
                        .firstName("Itay")
                        .lastName("Dor")
                        .email("id@gm.com")
                        .password("itay:)")
                        .build()
                ,
                Student.builder()
                        .firstName("Atara")
                        .lastName("ST")
                        .email("a33@gm.com")
                        .password("HappyDay")
                        .build()
                ,
                Student.builder()
                        .firstName("Marco")
                        .lastName("no-name")
                        .email("marco@gm.com")
                        .password("Where is my mom?")
                        .build()
                ,
                Student.builder()
                        .firstName("z")
                        .lastName("z")
                        .email("z@z.z")
                        .password("z")
                        .build()
        );
        return args -> {
            studentRepository.saveAll(students);
        };
    }
}
