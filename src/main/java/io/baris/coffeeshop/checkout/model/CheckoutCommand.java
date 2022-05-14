package io.baris.coffeeshop.checkout.model;

import io.baris.coffeeshop.event.model.Command;
import io.baris.coffeeshop.event.model.EventType;
import lombok.Builder;
import lombok.Value;

import static io.baris.coffeeshop.event.model.EventType.CHECKOUT;

/**
 * Command for checkout
 */
@Value
@Builder
public class CheckoutCommand implements Command<ShoppingCart> {

    EventType eventType = CHECKOUT;

    ShoppingCart shoppingCart;

    @Override
    public ShoppingCart getEventObject() {
        return shoppingCart;
    }
}
