package io.baris.coffeeshop.testing;

import io.baris.coffeeshop.CoffeeShopApplication;
import io.baris.coffeeshop.system.config.CoffeeShopConfig;
import io.baris.coffeeshop.system.utils.SystemUtils;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit5.DropwizardAppExtension;

/**
 * Junit rule to start the application
 */
public class AppBootstrapExtension extends DropwizardAppExtension<CoffeeShopConfig> {

    public AppBootstrapExtension(
        final String configPath,
        final String databaseUrl
    ) {
        super(
            CoffeeShopApplication.class,
            SystemUtils.resourceFilePath(configPath),
            ConfigOverride.config("database.url", databaseUrl)
        );
    }
}
