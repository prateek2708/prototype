package com.zoomsystems.replenisher.poc.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.zoomsystems.replenisher.poc.exceptions.ReplenisherEventException;
import com.zoomsystems.replenisher.poc.models.Event;
import com.zoomsystems.replenisher.poc.models.EventState;
import com.zoomsystems.replenisher.poc.models.EventType;
import com.zoomsystems.replenisher.poc.models.PublishEventResponse;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class EventRepository {
    @Autowired
    private Firestore firestore;

    public PublishEventResponse publishEvent(Event event) {
        try {
            DocumentReference documentReference = firestore.collection("zoom-test/replenisher/events").document();
            event.setEventId(documentReference.getId());
            documentReference.set(event).get();
            return new PublishEventResponse(documentReference.getId());
        } catch (InterruptedException e) {
            log.error("Failed to publish event", e);
            throw new ReplenisherEventException("Failed to Publish Event");
        } catch (ExecutionException e) {
            log.error("Failed to publish event", e);
            throw new ReplenisherEventException("Failed to Publish Event");
        } catch (Exception e) {
            throw new ReplenisherEventException("Failed to Publish Event");
        }
    }

    public PublishEventResponse updateEvent(Event event) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("eventState", event.getEventState().name());
            WriteResult writeResult = firestore.document(String.format("zoom-test/replenisher/events/%s", event.getEventId())).update(map).get();
            writeResult.getUpdateTime();
            return new PublishEventResponse(event.getEventId());
        } catch (InterruptedException e) {
            log.error("Failed to Update event", e);
            throw new ReplenisherEventException("Failed to Update Event");
        } catch (ExecutionException e) {
            log.error("Failed to publish event", e);
            throw new ReplenisherEventException("Failed to Update Event");
        } catch (Exception e) {
            throw new ReplenisherEventException("Failed to Update Event");
        }
    }

    public Event getEvent(String eventId) {
        try {
            DocumentSnapshot documentSnapshot = firestore.document("zoom-test/replenisher/events/" + eventId).get().get();
            Map<String, Object> data = documentSnapshot.getData();
            return buildEvent(eventId, data);
        } catch (InterruptedException e) {
            log.error("Failed to get event", e);
            throw new RuntimeException("Failed to get event");
        } catch (ExecutionException e) {
            log.error("Failed to get event", e);
            throw new RuntimeException("Failed to get event");
        } catch (Exception e) {
            log.error("Failed to get event", e);
            throw new RuntimeException("Failed to get event");
        }
    }

    private Event buildEvent(String eventId, Map<String, Object> data) {
        Event event = new Event();
        event.setEventId(eventId);
        event.setEventState(EventState.getEventState((String) data.get("eventState")));
        event.setEventType(EventType.getEventType((String) data.get("eventType")));
        event.setStoreId((String) data.get("storeId"));
        event.setMessage((String) data.get("message"));
        return event;
    }
}
