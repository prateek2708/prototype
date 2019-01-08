package com.demo.firestoresync.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class Application {


    private static Firestore firestore;

    private Application() {
        InputStream serviceAccount = null;
        try {
            serviceAccount = this.getClass().getResourceAsStream("/service_account.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);

            firestore = FirestoreClient.getFirestore();
            /*try {
                // somehow without this it does not work on the local server
                firestore.getOptions().getCredentials().getRequestMetadata();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void intialize() {
        new Application();
    }

    // Use a service account
    public static Firestore getFirestore() {
        if (firestore == null) {
            intialize();
        }
        return firestore;

    }
}
