package com.project.pagu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PaguApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaguApplication.class, args);
    }

}
