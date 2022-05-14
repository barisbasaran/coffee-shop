package io.baris.coffeeshop.cqrs.command;

import io.baris.coffeeshop.checkout.model.ShoppingCart;
import io.baris.coffeeshop.cqrs.event.model.EventType;
import lombok.Builder;
import lombok.Value;

import static io.baris.coffeeshop.cqrs.event.model.EventType.CHECKOUT;

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
