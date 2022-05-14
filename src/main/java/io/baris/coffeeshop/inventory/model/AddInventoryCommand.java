package io.baris.coffeeshop.inventory.model;

import io.baris.coffeeshop.event.model.Command;
import io.baris.coffeeshop.event.model.EventType;
import lombok.Builder;
import lombok.Value;

import static io.baris.coffeeshop.event.model.EventType.ADD_INVENTORY;

/**
 * Command for checkout
 */
@Value
@Builder
public class AddInventoryCommand implements Command<AddInventory> {

    EventType eventType = ADD_INVENTORY;

    AddInventory addInventory;

    @Override
    public AddInventory getEventObject() {
        return addInventory;
    }
}
