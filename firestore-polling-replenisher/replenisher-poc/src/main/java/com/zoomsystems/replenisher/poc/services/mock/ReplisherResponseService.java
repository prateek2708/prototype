/*package com.zoomsystems.replenisher.poc.services;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.database.annotations.Nullable;
import com.zoomsystems.replenisher.poc.models.Event;
import com.zoomsystems.replenisher.poc.models.EventState;

import lombok.extern.slf4j.Slf4j;

//@Service
@Slf4j
public class ReplisherResponseService {
    @Autowired
    private Firestore firestore;
    @Autowired
    private EventService eventService;

    @PostConstruct
    public void intialize() {
        listenForEvents();
    }

    private void listenForEvents() {
        this.getOrderDocumentQuery().addSnapshotListener(new EventListener<QuerySnapshot>() {
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirestoreException e) {
                if (e != null) {
                    log.exceptions("Failed to listen to firestore events:" + e);
                } else {
                    Iterator var3 = snapshots.getDocumentChanges().iterator();

                    while (var3.hasNext()) {
                        DocumentChange dc = (DocumentChange) var3.next();
                        QueryDocumentSnapshot document = dc.getDocument();
                        switch (dc.getType()) {
                            case ADDED:
                            case MODIFIED:
                                notifyClient(dc.getDocument());
                                break;
                        }
                    }
                }
            }
        });
    }

    private void notifyClient(QueryDocumentSnapshot document) {
        Event event = new Event();
        event.setEventId(document.getId());
        if (notifyClient(document.getData())) {
            event.setEventState(EventState.PROCESSED);
            eventService.updateEvent(event);
        } else {
            event.setEventState(EventState.FAILED);
            eventService.updateEvent(event);
        }
    }

    private boolean notifyClient(Map<String, Object> data) {
        try {
            doSomething();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    private void doSomething() throws InterruptedException {
        Thread.sleep(1000);
    }

    private Query getOrderDocumentQuery() {
        return firestore.collection("zoom-test/replenisher/events").whereEqualTo("eventState", EventState.IN_PROGRESS.name());
    }
}*/
