package io.baris.coffeeshop.cqrs.project;

import io.baris.coffeeshop.cqrs.event.model.Event;
import io.baris.coffeeshop.inventory.InventoryManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.baris.coffeeshop.cqrs.event.EventMapper.mapToAddStock;
import static io.baris.coffeeshop.cqrs.event.EventMapper.mapToShoppingCart;

@Slf4j
@RequiredArgsConstructor
public class Projector {

    private final InventoryManager inventoryManager;

    public void projectData(final Event event) {
        switch (event.getEventType()) {
            case CHECKOUT -> inventoryManager.updateInventory(mapToShoppingCart(event));
            case ADD_STOCK -> inventoryManager.updateInventory(mapToAddStock(event));
            default -> log.info("No service found for key={}", event.getEventType());
        }
    }
}
