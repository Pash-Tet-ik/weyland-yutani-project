package com.weyland.yutani.bishop;

import com.weyland.yutani.bishop.service.DiagnosticService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BishopPrototypeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BishopPrototypeApplication.class, args);
    }

    @Bean
    public CommandLineRunner startupDiagnostic(DiagnosticService diagnosticService) {
        return args -> diagnosticService.runSystemCheck("Power Core", 1);
    }
}
