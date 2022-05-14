package io.baris.coffeeshop.system.config;

import io.baris.coffeeshop.product.model.ProductUnit;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductConfig {
    ProductUnit unit;
    int quantity;
}
