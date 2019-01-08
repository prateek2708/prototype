package com.demo.firestoresync.app.com.demo.firestoresync.client;

import java.util.ArrayList;
import java.util.List;

import com.demo.firestoresync.app.Application;
import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.database.annotations.Nullable;

public class DemoClient {
    Firestore db = Application.getFirestore();

    public static void main(String[] args) {
        DemoClient demoClient = new DemoClient();
        demoClient.documentSnapshot();
        demoClient.QuerySnapshot();
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void documentSnapshot() {
        DocumentReference docRef = db.collection("J&J").document("CoreA").collection("order").document("CoreA - 2018-12-26T13:26:11_257Z");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    System.out.println("Current data: " + snapshot.getData());
                } else {
                    System.out.println("Current data: null");
                }
            }
        });
    }

    private void QuerySnapshot() {
        db.collection("J&J").document("CoreA").collection("order")
                .whereEqualTo("orderStatus", "disc")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirestoreException e) {
                        if (e != null) {
                            System.err.println("Listen failed:" + e);
                            return;
                        }

                        List<String> orders = new ArrayList<String>();
                        for (DocumentSnapshot doc : snapshots) {
                            if (doc.get("orderId") != null) {
                                orders.add(doc.getString("orderId"));
                            }
                        }
                        System.out.println("open orders" + orders);
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    System.out.println("New Order: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    System.out.println("Modified order: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    System.out.println("Removed order: " + dc.getDocument().getData());
                                    break;
                                default:
                                    break;
                            }
                        }

                    }

                });
    }
}