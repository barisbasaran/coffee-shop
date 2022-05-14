package io.baris.coffeeshop.inventory.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Min;

/**
 * Represents the request to add product to the inventory
 */
@Builder
@Value
public class AddInventory {

    String product;
    @Min(value = 1, message = "Quantity must be greater than zero")
    int quantity;
}
