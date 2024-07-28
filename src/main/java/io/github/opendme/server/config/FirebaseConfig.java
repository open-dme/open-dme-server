package io.github.opendme.server.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    @Bean
    public FirebaseApp firebaseApp(GoogleCredentials credentials) {
        FirebaseOptions options = FirebaseOptions.builder().setCredentials(credentials).build();
        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public GoogleCredentials googleCredentials(@Value("${fcm.service-account:classpath:firebase-service-account.json}") Resource resource) {
        try {
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    return GoogleCredentials.fromStream(is);
                }
            } else {
                return GoogleCredentials.getApplicationDefault();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
