package com.zoomsystems.replenisher.poc.repositories;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.zoomsystems.replenisher.poc.models.Event;

@Repository
public class MessageRepository {
    private Map<String, Event> eventMessages = new ConcurrentHashMap<>();

    public void putMessage(String eventId, Event event) {
        eventMessages.put(eventId, event);
    }

    public Event getMessage(String eventId) {
        return eventMessages.get(eventId);
    }

    public Event removeMessage(String eventId) {
        return eventMessages.remove(eventId);
    }
}

