package io.baris.coffeeshop.stock.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockProduct {

    String product;
    int quantity;
    ProductUnit unit;
}
