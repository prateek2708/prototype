package com.zoomsystems.replenisher.poc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoomsystems.replenisher.poc.models.InventoryRequest;
import com.zoomsystems.replenisher.poc.models.InventoryResponse;
import com.zoomsystems.replenisher.poc.repositories.InventoryRepository;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public InventoryResponse updateInventory(InventoryRequest inventoryRequest) {
        return inventoryRepository.updateInventory(inventoryRequest);
    }
}
