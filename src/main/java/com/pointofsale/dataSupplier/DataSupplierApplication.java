package com.pointofsale.dataSupplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DataSupplierApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataSupplierApplication.class, args);
    }

}
