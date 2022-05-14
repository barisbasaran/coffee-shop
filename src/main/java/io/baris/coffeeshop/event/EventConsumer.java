package io.baris.coffeeshop.event;

import io.baris.coffeeshop.event.model.Event;
import io.baris.coffeeshop.inventory.InventoryManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.baris.coffeeshop.event.EventMapper.mapToAddStock;
import static io.baris.coffeeshop.event.EventMapper.mapToShoppingCart;

@Slf4j
@RequiredArgsConstructor
public class EventConsumer {

    private final EventManager eventManager;
    private final InventoryManager inventoryManager;

    public void handleEvent(final Event event) {
        eventManager.createEvent(event);

        switch (event.getEventType()) {
            case CHECKOUT -> inventoryManager
                .updateInventory(mapToShoppingCart(event));
            case ADD_STOCK -> inventoryManager
                .updateInventory(mapToAddStock(event));
            default -> log.info("No handler found for event {}", event);
        }
    }
}
