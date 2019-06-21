package com.zoomsystems.replenisher.poc.repositories;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.Firestore;
import com.zoomsystems.replenisher.poc.models.Inventory;
import com.zoomsystems.replenisher.poc.models.InventoryRequest;
import com.zoomsystems.replenisher.poc.models.InventoryResponse;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class InventoryRepository {
    @Autowired
    private Firestore firestore;

    public InventoryResponse updateInventory(InventoryRequest inventoryRequest) {
        try {
            for (Inventory inventory : inventoryRequest.getInventories()) {
                firestore.document(String.format("zoom-test/%s/inventory/%s", inventoryRequest.getStoreId(), inventory.getSku())).set(inventory).get();
            }
            return new InventoryResponse("Inventory Updated Successfully");
        } catch (InterruptedException e) {
            log.error("Failed to update inventory for store: " + inventoryRequest.getStoreId(), e);
            return new InventoryResponse("Failed to update inventory for store: ");
        } catch (ExecutionException e) {
            log.error("Failed to update inventory for store: " + inventoryRequest.getStoreId(), e);
            return new InventoryResponse("Failed to update inventory for store: ");
        } catch (Exception e) {
            log.error("Failed to update inventory for store: " + inventoryRequest.getStoreId(), e);
            return new InventoryResponse("Failed to update inventory for store: ");
        }
    }
}
