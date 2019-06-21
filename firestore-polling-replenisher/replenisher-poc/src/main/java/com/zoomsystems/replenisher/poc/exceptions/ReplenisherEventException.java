package com.zoomsystems.replenisher.poc.exceptions;

public class ReplenisherEventException extends RuntimeException {
    public ReplenisherEventException(String failedToPublishEvent) {
        super(failedToPublishEvent);
    }
}
