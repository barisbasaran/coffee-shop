package io.baris.coffeeshop.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryProduct {

    String product;
    int quantity;
    ProductUnit unit;
}
