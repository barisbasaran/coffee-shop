package io.baris.coffeeshop.inventory;

import io.baris.coffeeshop.inventory.model.InventoryProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Serves inventory endpoints
 */
@Path("/inventory")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Slf4j
@RequiredArgsConstructor
public class InventoryResource {

    private final InventoryManager inventoryManager;

    @Path("/products")
    @GET
    public List<InventoryProduct> getInventoryProducts() {
        return inventoryManager.getInventoryProducts();
    }
}
