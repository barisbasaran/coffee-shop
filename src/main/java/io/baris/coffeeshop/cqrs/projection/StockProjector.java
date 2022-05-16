package io.baris.coffeeshop.cqrs.projection;

import io.baris.coffeeshop.cqrs.event.model.Event;
import io.baris.coffeeshop.stock.StockManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.baris.coffeeshop.cqrs.event.EventMapper.mapToAddStock;
import static io.baris.coffeeshop.cqrs.event.EventMapper.mapToShoppingCart;

@Slf4j
@RequiredArgsConstructor
public class StockProjector {

    private final StockManager stockManager;

    public void projectData(final Event event) {
        switch (event.getEventType()) {
            case CHECKOUT -> stockManager.updateStocks(mapToShoppingCart(event));
            case ADD_STOCK -> stockManager.updateStocks(mapToAddStock(event));
            default -> log.info("No service found for key={}", event.getEventType());
        }
    }
}
