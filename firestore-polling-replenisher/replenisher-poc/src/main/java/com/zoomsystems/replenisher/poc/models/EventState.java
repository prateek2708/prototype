package com.zoomsystems.replenisher.poc.models;

public enum EventState {
    PUBLISHED, IN_PROGRESS, FAILED, PROCESSED;

    public static EventState getEventState(String eventState) {
        for (EventState state : EventState.values()) {
            if (state.name().equals(eventState)) {
                return state;
            }
        }
        return null;
    }
}
