package io.baris.coffeeshop.inventory;

import io.baris.coffeeshop.event.EventManager;
import io.baris.coffeeshop.inventory.model.AddInventory;
import io.baris.coffeeshop.inventory.model.AddInventoryCommand;
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
@Path("/inventory")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Slf4j
@RequiredArgsConstructor
public class InventoryResource {

    private final EventManager eventManager;

    @PUT
    public Response addInventory(final @Valid AddInventory addInventory) {
        var addInventoryCommand = AddInventoryCommand.builder()
            .addInventory(addInventory)
            .build();
        eventManager
            .addEvent(addInventoryCommand)
            .orElseThrow(() -> new InternalServerErrorException("Adding to inventory failed"));

        return Response.accepted(addInventoryCommand).build();
    }
}
