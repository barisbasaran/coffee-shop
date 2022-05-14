package io.baris.coffeeshop;

import io.baris.coffeeshop.checkout.CheckoutResource;
import io.baris.coffeeshop.event.EventManager;
import io.baris.coffeeshop.event.EventResource;
import io.baris.coffeeshop.homepage.HomepageResource;
import io.baris.coffeeshop.inventory.InventoryManager;
import io.baris.coffeeshop.stock.StockResource;
import io.baris.coffeeshop.system.config.CoffeeShopConfig;
import io.baris.coffeeshop.system.CoffeeShopHealthCheck;
import io.baris.coffeeshop.system.utils.SystemUtils;
import io.baris.coffeeshop.system.kafka.KafkaEventConsumer;
import io.baris.coffeeshop.system.kafka.KafkaEventProducer;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.util.concurrent.Executors;

import static io.baris.coffeeshop.system.config.CorsConfigurer.configureCors;
import static io.baris.coffeeshop.system.utils.PostgreUtils.applySqlScript;
import static io.baris.coffeeshop.system.kafka.KafkaEventConsumer.createKafkaConsumer;
import static io.baris.coffeeshop.system.kafka.KafkaEventProducer.createKafkaProducer;

/**
 * Vet service application class to bootstrap the application
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

        var kafkaEventProducer = new KafkaEventProducer(createKafkaProducer());
        var eventManager = new EventManager(jdbi);
        var inventoryManager = new InventoryManager(eventManager, jdbi, configuration.getInventoryConfig());

        // register resources
        environment.jersey().register(new CheckoutResource(kafkaEventProducer));
        environment.jersey().register(new StockResource(kafkaEventProducer));
        environment.jersey().register(new EventResource(eventManager));
        environment.jersey().register(new HomepageResource());
        environment.jersey().register(new OpenApiResource());

        Executors.newSingleThreadExecutor().submit(() ->
            new KafkaEventConsumer(createKafkaConsumer(), eventManager, inventoryManager)
                .subscribe()
        );
    }
}
