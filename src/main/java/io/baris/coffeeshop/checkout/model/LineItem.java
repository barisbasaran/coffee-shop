package io.baris.coffeeshop.checkout.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Min;

/**
 * Represents a line item in a shopping cart
 */
@Value
@Builder
public class LineItem {

    String product;
    @Min(value = 1, message = "Quantity must be greater than zero")
    int quantity;
}
