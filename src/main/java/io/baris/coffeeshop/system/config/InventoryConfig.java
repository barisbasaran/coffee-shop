package io.baris.coffeeshop.system.config;

import lombok.Data;

import java.util.Map;
import java.util.Optional;

@Data
public class InventoryConfig {

    Map<String, Integer> productQuantities;

    public int getQuantity(final String product) {
        return Optional
            .ofNullable(productQuantities.get(product))
            .orElse(1);
    }
}
