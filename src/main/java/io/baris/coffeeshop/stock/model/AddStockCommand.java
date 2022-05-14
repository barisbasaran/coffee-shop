package io.baris.coffeeshop.stock.model;

import io.baris.coffeeshop.event.model.Command;
import io.baris.coffeeshop.event.model.EventType;
import lombok.Builder;
import lombok.Value;

import static io.baris.coffeeshop.event.model.EventType.ADD_STOCK;

/**
 * Command for checkout
 */
@Value
@Builder
public class AddStockCommand implements Command<AddStock> {

    EventType eventType = ADD_STOCK;

    AddStock addStock;

    @Override
    public AddStock getEventObject() {
        return addStock;
    }
}
