package io.baris.coffeeshop.checkout;

import io.baris.coffeeshop.checkout.model.CheckoutCommand;
import io.baris.coffeeshop.checkout.model.ShoppingCart;
import io.baris.coffeeshop.event.EventManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Serves checkout endpoints
 */
@Path("/checkout")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Slf4j
@RequiredArgsConstructor
public class CheckoutResource {

    private final EventManager eventManager;

    @PUT
    public Response checkout(final @Valid ShoppingCart shoppingCart) {
        var checkoutCommand = CheckoutCommand.builder()
            .shoppingCart(shoppingCart)
            .build();
        eventManager
            .addEvent(checkoutCommand)
            .orElseThrow(() -> new InternalServerErrorException("Checkout failed"));

        return Response.accepted(checkoutCommand).build();
    }
}
