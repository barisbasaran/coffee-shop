package io.baris.coffeeshop.checkout;

import io.baris.coffeeshop.checkout.model.ShoppingCart;
import io.baris.coffeeshop.cqrs.command.CheckoutCommand;
import io.baris.coffeeshop.cqrs.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Serves checkout endpoints.json
 */
@Path("/checkout")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Slf4j
@RequiredArgsConstructor
public class CheckoutResource {

    private final CommandHandler commandHandler;

    @PUT
    public CheckoutCommand checkout(final @Valid ShoppingCart shoppingCart) {
        var checkoutCommand = CheckoutCommand.builder()
            .shoppingCart(shoppingCart)
            .build();
        commandHandler.handleCommand(checkoutCommand);

        return checkoutCommand;
    }
}
