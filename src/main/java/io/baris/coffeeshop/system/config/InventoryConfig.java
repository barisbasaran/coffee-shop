package io.baris.coffeeshop.system.config;

import io.baris.coffeeshop.product.model.ProductUnit;
import lombok.Value;

import java.util.Map;
import java.util.Optional;

@Value
public class InventoryConfig {

    private static ProductConfig DEFAULT_PRODUCT_CONFIG = new ProductConfig(ProductUnit.ITEM, 1);

    Map<String, ProductConfig> productConfig;

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
