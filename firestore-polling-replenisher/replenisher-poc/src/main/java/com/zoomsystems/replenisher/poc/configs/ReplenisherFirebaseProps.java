package com.zoomsystems.replenisher.poc.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "firebase.replenisher")
@Getter
@Setter
public class ReplenisherFirebaseProps {
    private String databaseUrl;
    private String configPath;
}
