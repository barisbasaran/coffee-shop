package io.baris.coffeeshop.system.config;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Application configuration
 */
@Data
public class CoffeeShopConfig extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @NotNull
    private DatabaseConfig databaseConfig;

    @NotNull
    private InventoryConfig inventoryConfig;
}
