package com.example.mynamz;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.mynamz.service.BusinessCardService;

@SpringBootApplication
public class MynamzApplication {
    public static void main(String[] args) {
        SpringApplication.run(MynamzApplication.class, args);
    }
    
   
}