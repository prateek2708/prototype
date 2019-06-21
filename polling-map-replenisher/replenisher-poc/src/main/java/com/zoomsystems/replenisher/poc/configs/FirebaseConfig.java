package com.zoomsystems.replenisher.poc.configs;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.replenisher.database.url}")
    private String databaseUrl;

    @Value("${firebase.replenisher.config.path}")
    private String configPath;

    @Bean
    public DatabaseReference firebaseDatabse() {
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
        return firebase;
    }

    @Bean
    public Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    @PostConstruct
    public void initializeApp() throws IOException {

        // Fetch the service account key JSON file contents
        InputStream serviceAccount = FirebaseConfig.class.getClassLoader().getResourceAsStream(configPath);

        // Initialize the app with a service account, granting admin privileges
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(databaseUrl)
                .build();

        FirebaseApp.initializeApp(options);

    }
}