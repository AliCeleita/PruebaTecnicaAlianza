package com.alianza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class PruebaTecnicaAlianzaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PruebaTecnicaAlianzaApplication.class, args);
    }

}
