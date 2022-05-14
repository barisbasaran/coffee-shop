package io.baris.coffeeshop;

import io.baris.coffeeshop.checkout.CheckoutResource;
import io.baris.coffeeshop.event.EventManager;
import io.baris.coffeeshop.event.EventResource;
import io.baris.coffeeshop.homepage.HomepageResource;
import io.baris.coffeeshop.inventory.InventoryResource;
import io.baris.coffeeshop.system.CoffeeShopConfiguration;
import io.baris.coffeeshop.system.CoffeeShopHealthCheck;
import io.baris.coffeeshop.system.SystemUtils;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import static io.baris.coffeeshop.system.CorsConfigurer.configureCors;
import static io.baris.coffeeshop.system.PostgreUtils.applySqlScript;

/**
 * Vet service application class to bootstrap the application
 */
public class CoffeeShopApplication extends Application<CoffeeShopConfiguration> {

    public static void main(final String[] args) throws Exception {
        new CoffeeShopApplication().run(args);
    }

    @Override
    public String getName() {
        return "Coffee Shop";
    }

    @Override
    public void initialize(final Bootstrap<CoffeeShopConfiguration> bootstrap) {
        SystemUtils.configureObjectMapper(bootstrap.getObjectMapper());
    }

    @Override
    public void run(
        final CoffeeShopConfiguration configuration,
        final Environment environment
    ) {
        environment.healthChecks().register("health", new CoffeeShopHealthCheck());

        initialiseBeans(configuration, environment);

        configureCors(environment);
    }

    private void initialiseBeans(
        final CoffeeShopConfiguration configuration,
        final Environment environment
    ) {
        var jdbi = new JdbiFactory()
            .build(environment, configuration.getDatabase(), configuration.getDatabaseConfig().getName());
        jdbi.installPlugin(new SqlObjectPlugin());

        // initialize DB schema
        applySqlScript(jdbi, configuration.getDatabaseConfig().getInitScript());

        var eventManager = new EventManager(jdbi);

        // register resources
        environment.jersey().register(new CheckoutResource(eventManager));
        environment.jersey().register(new InventoryResource(eventManager));
        environment.jersey().register(new EventResource(eventManager));
        environment.jersey().register(new HomepageResource());
        environment.jersey().register(new OpenApiResource());
    }
}
