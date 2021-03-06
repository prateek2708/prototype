package com.zoomsystems.replenisher.poc.services;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.database.annotations.Nullable;
import com.zoomsystems.replenisher.poc.models.Event;
import com.zoomsystems.replenisher.poc.models.EventState;
import com.zoomsystems.replenisher.poc.models.PublishEventResponse;
import com.zoomsystems.replenisher.poc.repositories.EventRepository;
import com.zoomsystems.replenisher.poc.repositories.MessageRepository;

@Service
public class EventService {

    private static final Integer EVENT_TIMEOUT = 15;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    MessageRepository messageRepository;

    public Event publishEvent(Event event) {
        //publish event
        PublishEventResponse eventResponse = eventRepository.publishEvent(event);
        //process event
        return processEvent(event, eventResponse);
    }

    private Event processEvent(Event event, PublishEventResponse eventResponse) {
        try {
            Event eventRes = listenForProcessedEvent(eventResponse.getEventId());
            eventRes.setEventState(EventState.PROCESSED);
            updateEvent(eventRes);
            return eventRes;
        } catch (Exception e) {
            return buildAndUpdateFailureEventResponse(event, eventResponse, e.getMessage());
        }
    }

    private Event buildAndUpdateFailureEventResponse(Event event, PublishEventResponse eventResponse, String failureMessage) {
        event.setEventState(EventState.FAILED);
        event.setEventId(eventResponse.getEventId());
        event.setMessage(failureMessage);
        updateEvent(event);
        return event;
    }

    public PublishEventResponse updateEvent(Event event) {
        return eventRepository.updateEvent(event);

    }

    private Event listenForProcessedEvent(String eventId) {
        CompletableFuture<Event> future = CompletableFuture.supplyAsync(() -> {
            Event event = null;
            while (true) {
                event = messageRepository.getMessage(eventId);
                if (event != null && EventState.IN_PROGRESS.name().equals(event.getEventState().name())) {
                    messageRepository.removeMessage(eventId);
                    return event;
                }
            }
        });
        try {
            return future.get(EVENT_TIMEOUT, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            future.cancel(true);
            throw new RuntimeException("Failed to process Response", e);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new RuntimeException("Requested Timed out", e);
        } catch (Exception e) {
            future.cancel(true);
            throw new RuntimeException("Internal Server error", e);
        }
    }

}
