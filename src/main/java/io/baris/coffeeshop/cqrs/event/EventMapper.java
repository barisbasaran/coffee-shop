package io.baris.coffeeshop.cqrs.event;

import io.baris.coffeeshop.checkout.model.ShoppingCart;
import io.baris.coffeeshop.cqrs.event.model.Event;
import io.baris.coffeeshop.stock.model.AddStock;

import static io.baris.coffeeshop.system.utils.SystemUtils.toObject;

/**
 * Maps event model objects
 */
public class EventMapper {

    public static ShoppingCart mapToShoppingCart(final Event event) {
        return toObject(event.getEvent(), ShoppingCart.class);
    }

    public static AddStock mapToAddStock(final Event event) {
        return toObject(event.getEvent(), AddStock.class);
    }
}
