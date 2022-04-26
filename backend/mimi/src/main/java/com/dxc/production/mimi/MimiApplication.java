package com.dxc.production.mimi;

import com.dxc.production.mimi.service.FileStorageServiceInterface;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@SpringBootApplication
@EnableJpaAuditing
public class MimiApplication  {

    @Resource
    FileStorageServiceInterface fileStorageServiceInterface;

    public static void main(String[] args) {
        SpringApplication.run(MimiApplication.class, args);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
