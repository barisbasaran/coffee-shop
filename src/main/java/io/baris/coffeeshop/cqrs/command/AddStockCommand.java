package io.baris.coffeeshop.cqrs.command;

import io.baris.coffeeshop.cqrs.event.model.EventType;
import io.baris.coffeeshop.stock.model.AddStock;
import lombok.Builder;
import lombok.Value;

import static io.baris.coffeeshop.cqrs.event.model.EventType.ADD_STOCK;

/**
 * Command for adding stock
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
