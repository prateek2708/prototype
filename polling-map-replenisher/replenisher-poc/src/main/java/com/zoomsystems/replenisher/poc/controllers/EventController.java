package com.zoomsystems.replenisher.poc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoomsystems.replenisher.poc.models.Event;
import com.zoomsystems.replenisher.poc.services.EventService;

@RestController
@RequestMapping("replenisher/v1/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping
    public Event publishEvent(@RequestBody Event event) {
        return eventService.publishEvent(event);
    }
}
