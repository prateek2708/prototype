package com.zoomsystems.replenisher.poc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoomsystems.replenisher.poc.models.InventoryRequest;
import com.zoomsystems.replenisher.poc.models.InventoryResponse;
import com.zoomsystems.replenisher.poc.services.InventoryService;

@RestController
@RequestMapping("replenisher/v1/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public InventoryResponse replenishInventory(@RequestBody InventoryRequest inventoryRequest) {
        return inventoryService.updateInventory(inventoryRequest);

    }
}
