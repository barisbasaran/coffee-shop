package io.baris.coffeeshop.checkout.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Represents the shopping cart
 */
@Builder
@Value
public class ShoppingCart {

    @NotEmpty
    List<LineItem> lineItems;
}
