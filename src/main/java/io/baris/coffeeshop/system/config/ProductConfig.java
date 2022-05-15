package io.baris.coffeeshop.system.config;

import io.baris.coffeeshop.inventory.model.ProductUnit;
import lombok.Value;

@Value
public class ProductConfig {
    ProductUnit unit;
    int quantity;
}