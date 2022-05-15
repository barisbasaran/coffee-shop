package io.baris.coffeeshop;

import io.baris.coffeeshop.checkout.CheckoutResource;
import io.baris.coffeeshop.cqrs.command.CommandHandler;
import io.baris.coffeeshop.cqrs.event.EventManager;
import io.baris.coffeeshop.cqrs.event.EventResource;
import io.baris.coffeeshop.cqrs.project.Projector;
import io.baris.coffeeshop.inventory.InventoryManager;
import io.baris.coffeeshop.inventory.InventoryResource;
import io.baris.coffeeshop.stock.StockResource;
import io.baris.coffeeshop.system.CoffeeShopHealthCheck;
import io.baris.coffeeshop.system.HomepageResource;
import io.baris.coffeeshop.system.config.CoffeeShopConfig;
import io.baris.coffeeshop.system.kafka.KafkaEventConsumer;
import io.baris.coffeeshop.system.kafka.KafkaEventProducer;
import io.baris.coffeeshop.system.utils.SystemUtils;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.util.concurrent.Executors;

import static io.baris.coffeeshop.system.config.CorsConfigurer.configureCors;
import static io.baris.coffeeshop.system.utils.PostgreUtils.applySqlScript;

/**
 * Main application class to bootstrap the application
 */
public class CoffeeShopApplication extends Application<CoffeeShopConfig> {

    public static void main(final String[] args) throws Exception {
        new CoffeeShopApplication().run(args);
    }

    @Override
    public String getName() {
        return "Coffee Shop";
    }

    @Override
    public void initialize(final Bootstrap<CoffeeShopConfig> bootstrap) {
        SystemUtils.configureObjectMapper(bootstrap.getObjectMapper());
    }

    @Override
    public void run(
        final CoffeeShopConfig configuration,
        final Environment environment
    ) {
        environment.healthChecks().register("health", new CoffeeShopHealthCheck());

        initialiseBeans(configuration, environment);

        configureCors(environment);
    }

    private void initialiseBeans(
        final CoffeeShopConfig configuration,
        final Environment environment
    ) {
        var jdbi = new JdbiFactory()
            .build(environment, configuration.getDatabase(), configuration.getDatabaseConfig().getName());
        jdbi.installPlugin(new SqlObjectPlugin());

        // initialize DB schema
        applySqlScript(jdbi, configuration.getDatabaseConfig().getInitScript());

        var kafkaEventProducer = new KafkaEventProducer(configuration);
        var eventManager = new EventManager(jdbi);
        var inventoryManager = new InventoryManager(eventManager, jdbi, configuration.getInventoryConfig());
        var commandHandler = new CommandHandler(eventManager, kafkaEventProducer);

        // register resources
        environment.jersey().register(new CheckoutResource(commandHandler));
        environment.jersey().register(new StockResource(commandHandler));
        environment.jersey().register(new EventResource(eventManager));
        environment.jersey().register(new InventoryResource(inventoryManager));
        environment.jersey().register(new HomepageResource());
        environment.jersey().register(new OpenApiResource());

        var projector = new Projector(inventoryManager);
        Executors.newSingleThreadExecutor().submit(() ->
            new KafkaEventConsumer(projector, configuration).subscribe()
        );
    }
}
