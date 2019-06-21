package com.zoomsystems.replenisher.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ReplenisherPocApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReplenisherPocApplication.class, args);
    }

}
