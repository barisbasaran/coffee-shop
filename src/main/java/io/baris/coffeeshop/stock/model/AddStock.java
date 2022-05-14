package io.baris.coffeeshop.stock.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

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
}
