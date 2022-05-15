package io.baris.coffeeshop.system.config;

import io.baris.coffeeshop.inventory.model.ProductUnit;
import lombok.Data;

import java.util.Map;
import java.util.Optional;

@Data
public class InventoryConfig {

    private static ProductConfig DEFAULT_PRODUCT_CONFIG = new ProductConfig(ProductUnit.ITEM, 1);

    private Map<String, ProductConfig> productConfig;

    public int getProductQuantity(final String product) {
        return getProductConfig(product).getQuantity();
    }

    public ProductUnit getProductUnit(String product) {
        return getProductConfig(product).getUnit();
    }

    private ProductConfig getProductConfig(String product) {
        return Optional
            .ofNullable(productConfig.get(product))
            .orElse(DEFAULT_PRODUCT_CONFIG);
    }
}
