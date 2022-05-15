package io.baris.coffeeshop.system.config;

import io.baris.coffeeshop.stock.model.ProductUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductConfig {
    private ProductUnit unit;
    private int quantity;
}
