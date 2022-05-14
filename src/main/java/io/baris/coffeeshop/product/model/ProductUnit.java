package io.baris.coffeeshop.product.model;

public enum ProductUnit {
    GR,
    ITEM;

    public static ProductUnit getProductUnit(final String product) {
        return switch (product) {
            case "coffee" -> GR;
            default -> ITEM;
        };
    }
}
