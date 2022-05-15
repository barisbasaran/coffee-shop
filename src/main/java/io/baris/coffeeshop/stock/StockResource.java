package io.baris.coffeeshop.stock;

import io.baris.coffeeshop.cqrs.command.CommandHandler;
import io.baris.coffeeshop.cqrs.command.AddStockCommand;
import io.baris.coffeeshop.stock.model.StockProduct;
import io.baris.coffeeshop.stock.model.AddStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Serves stock endpoints.json
 */
@Path("/stocks")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Slf4j
@RequiredArgsConstructor
public class StockResource {

    private final CommandHandler commandHandler;
    private final StockManager stockManager;

    @PUT
    public Response addStock(final @Valid AddStock addStock) {
        var addStockCommand = AddStockCommand.builder()
            .addStock(addStock)
            .build();
        commandHandler.handleCommand(addStockCommand);

        return Response.ok(addStockCommand).build();
    }

    @GET
    public List<StockProduct> getStocks() {
        return stockManager.getStocks();
    }

}
