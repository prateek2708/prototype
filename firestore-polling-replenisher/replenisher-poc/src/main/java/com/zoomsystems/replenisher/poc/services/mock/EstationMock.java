package com.zoomsystems.replenisher.poc.services.mock;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

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
import com.zoomsystems.replenisher.poc.services.EventService;

import lombok.extern.slf4j.Slf4j;

//@Service
@Slf4j
public class EstationMock {
    @Autowired
    private Firestore firestore;
    @Autowired
    private EventService eventService;

    @PostConstruct
    public void intialize() {
        listenForEvents();
    }

    private static final String STORE_ID = "CoreA";

    private void listenForEvents() {
        this.getOrderDocumentQuery().addSnapshotListener(new EventListener<QuerySnapshot>() {
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirestoreException e) {
                if (e != null) {
                    log.error("Failed to listen to firestore events:" + e);
                } else {
                    Iterator var3 = snapshots.getDocumentChanges().iterator();

                    while (var3.hasNext()) {
                        DocumentChange dc = (DocumentChange) var3.next();
                        QueryDocumentSnapshot document = dc.getDocument();
                        switch (dc.getType()) {
                            case ADDED:
                                processEvent(dc.getDocument());
                                break;
                        }
                    }
                }
            }
        });
    }

    private void processEvent(QueryDocumentSnapshot document) {
        Event event = new Event();
        event.setEventId(document.getId());
        event.setStoreId(STORE_ID);
        if (processEvent(document.getData())) {
            event.setEventState(EventState.IN_PROGRESS);
            eventService.updateEvent(event);
        } else {
            event.setEventState(EventState.FAILED);
            eventService.updateEvent(event);
        }
    }

    private boolean processEvent(Map<String, Object> data) {
        try {
            doSomething();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    private void doSomething() {

    }

    private Query getOrderDocumentQuery() {
        return firestore.collection("zoom-test/replenisher/events").whereEqualTo("storeId", STORE_ID).whereEqualTo("eventState", EventState.PUBLISHED.name());
    }
}
