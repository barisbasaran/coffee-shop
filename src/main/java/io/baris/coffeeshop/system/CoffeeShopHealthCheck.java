package io.baris.coffeeshop.system;

import com.codahale.metrics.health.HealthCheck;

/**
 * Health check for the application
 */
public class CoffeeShopHealthCheck extends HealthCheck {

    @Override
    protected Result check() {
        return Result.healthy();
    }
}
