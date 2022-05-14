package io.baris.coffeeshop.stock.model;

import io.baris.coffeeshop.product.model.ProductUnit;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Represents the request to add stock
 */
@Builder
@Value
public class AddStock {

    @NotEmpty
    String product;

    @Min(value = 1)
    int quantity;

    @NotNull
    ProductUnit unit;
}
