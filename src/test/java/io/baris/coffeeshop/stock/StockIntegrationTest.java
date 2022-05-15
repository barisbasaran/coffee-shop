package io.baris.coffeeshop.stock;

import io.baris.coffeeshop.cqrs.command.AddStockCommand;
import io.baris.coffeeshop.stock.model.AddStock;
import io.baris.coffeeshop.testing.AppBootstrapExtension;
import io.baris.coffeeshop.testing.DbResetExtension;
import io.baris.coffeeshop.testing.PostgreExtension;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.ws.rs.client.Entity;

import java.time.Instant;

import static io.baris.coffeeshop.cqrs.event.model.EventType.ADD_STOCK;
import static io.baris.coffeeshop.testing.TestUtils.TEST_CONFIG;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class StockIntegrationTest {

    @RegisterExtension
    @Order(0)
    public final static PostgreExtension postgre = new PostgreExtension(TEST_CONFIG);

    @RegisterExtension
    @Order(1)
    public final static AppBootstrapExtension app = new AppBootstrapExtension(TEST_CONFIG, postgre.getDatabaseUrl());

    @RegisterExtension
    public DbResetExtension dbReset = new DbResetExtension(postgre.getJdbi());

    @Test
    public void addStock_Success() {
        // arrange
        var now = Instant.now();

        // act
        var addStock = AddStock.builder()
            .product("cake")
            .quantity(2)
            .build();
        var addStockCommand = app.client()
            .target(getTargetUrl())
            .path("stocks")
            .request()
            .put(Entity.json(addStock), AddStockCommand.class);

        // assert
        assertThat(addStockCommand).isNotNull();

        // verify DB changes
        var events = postgre.getAllEvents();
        assertThat(events).hasSize(1);
        var event = events.get(0);
        assertThat(event.getEventTime()).isAfterOrEqualTo(now);
        assertThat(event.getEventType()).isEqualTo(ADD_STOCK);
        assertThat(event.getEvent()).isEqualTo("{\"product\":\"cake\",\"quantity\":2}");
    }

    private String getTargetUrl() {
        return "http://localhost:%d".formatted(app.getLocalPort());
    }
}
