package io.baris.coffeeshop.stock;

import io.baris.coffeeshop.stock.model.AddStock;
import io.baris.coffeeshop.stock.model.AddStockCommand;
import io.baris.coffeeshop.system.kafka.KafkaEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Serves checkout endpoints
 */
@Path("/stocks")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Slf4j
@RequiredArgsConstructor
public class StockResource {

    private final KafkaEventProducer kafkaEventProducer;

    @PUT
    public Response addStock(final @Valid AddStock addStock) {
        var addStockCommand = AddStockCommand.builder()
            .addStock(addStock)
            .build();
        kafkaEventProducer.publishEvent(addStockCommand);

        return Response.accepted(addStockCommand).build();
    }
}
