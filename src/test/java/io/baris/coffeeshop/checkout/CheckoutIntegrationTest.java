package io.baris.coffeeshop.checkout;

import io.baris.coffeeshop.checkout.model.LineItem;
import io.baris.coffeeshop.checkout.model.ShoppingCart;
import io.baris.coffeeshop.cqrs.command.CheckoutCommand;
import io.baris.coffeeshop.testing.AppBootstrapExtension;
import io.baris.coffeeshop.testing.DbResetExtension;
import io.baris.coffeeshop.testing.PostgreExtension;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.ws.rs.client.Entity;
import java.time.Instant;
import java.util.List;

import static io.baris.coffeeshop.cqrs.event.model.EventType.CHECKOUT;
import static io.baris.coffeeshop.testing.TestUtils.TEST_CONFIG;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CheckoutIntegrationTest {

    @RegisterExtension
    @Order(0)
    public final static PostgreExtension postgre = new PostgreExtension(TEST_CONFIG);

    @RegisterExtension
    @Order(1)
    public final static AppBootstrapExtension app = new AppBootstrapExtension(TEST_CONFIG, postgre.getDatabaseUrl());

    @RegisterExtension
    public DbResetExtension dbReset = new DbResetExtension(postgre.getJdbi());

    @Test
    public void checkout_Success() {
        // arrange
        var now = Instant.now();

        // act
        var shoppingCart = ShoppingCart.builder()
            .lineItems(List.of(
                LineItem.builder().product("coffee").quantity(2).build(),
                LineItem.builder().product("cake").quantity(1).build()
            ))
            .build();
        var checkoutCommand = app.client()
            .target(getTargetUrl())
            .path("checkout")
            .request()
            .put(Entity.json(shoppingCart), CheckoutCommand.class);

        // assert
        assertThat(checkoutCommand).isNotNull();

        // verify DB changes
        var events = postgre.getAllEvents();
        assertThat(events).hasSize(1);
        var event = events.get(0);
        assertThat(event.getEventTime()).isAfterOrEqualTo(now);
        assertThat(event.getEventType()).isEqualTo(CHECKOUT);
        assertThat(event.getEvent()).isEqualTo(
            "{\"lineItems\":[{\"product\":\"coffee\",\"quantity\":2},{\"product\":\"cake\",\"quantity\":1}]}"
        );
    }

    private String getTargetUrl() {
        return "http://localhost:%d".formatted(app.getLocalPort());
    }
}
