package com.zoomsystems.replenisher.poc.models;

public enum EventType {

    OPEN_DOOR;

    public static EventType getEventType(String eventType) {
        for (EventType type : EventType.values()) {
            if (type.name().equals(eventType)) {
                return type;
            }
        }
        return null;
    }
}
