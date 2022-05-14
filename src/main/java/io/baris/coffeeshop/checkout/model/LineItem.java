package io.baris.coffeeshop.checkout.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * Represents a line item in a shopping cart
 */
@Value
@Builder
public class LineItem {

    @NotEmpty
    String product;

    @Min(value = 1)
    int quantity;
}
