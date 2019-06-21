package com.zoomsystems.replenisher.poc.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private String eventId;
    private EventType eventType;
    private String storeId;
    private EventState eventState;
    private String message;
}
