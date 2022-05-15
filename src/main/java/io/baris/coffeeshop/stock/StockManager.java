package io.baris.coffeeshop.stock;

import io.baris.coffeeshop.checkout.model.LineItem;
import io.baris.coffeeshop.checkout.model.ShoppingCart;
import io.baris.coffeeshop.cqrs.event.EventManager;
import io.baris.coffeeshop.stock.model.StockProduct;
import io.baris.coffeeshop.stock.model.AddStock;
import io.baris.coffeeshop.system.config.StocksConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

import static io.baris.coffeeshop.cqrs.event.EventMapper.mapToAddStock;
import static io.baris.coffeeshop.cqrs.event.EventMapper.mapToShoppingCart;

/**
 * Manages stocks
 */
@Slf4j
@RequiredArgsConstructor
public class StockManager {

    private final EventManager eventManager;
    private final Jdbi jdbi;
    private final StocksConfig stocksConfig;

    public List<StockProduct> getStocks() {
        return jdbi.withExtension(StockRepository.class, StockRepository::getStocks);
    }

    public void updateStocks(final ShoppingCart shoppingCart) {
        shoppingCart.getLineItems().stream()
            .map(LineItem::getProduct)
            .forEach(this::updateTotalQuantity);
    }

    public void updateStocks(final AddStock addStock) {
        updateTotalQuantity(addStock.getProduct());
    }

    private void updateTotalQuantity(final String product) {
        var totalQuantity = calculateTotalQuantity(product);

        var stockProduct = StockProduct.builder()
            .product(product)
            .quantity(totalQuantity)
            .unit(stocksConfig.getProductUnit(product))
            .build();
        jdbi.withExtension(StockRepository.class, dao ->
            dao.updateStocks(stockProduct));
    }

    private int calculateTotalQuantity(final String product) {
        int totalQuantity = 0;
        for (var event : eventManager.getAllEvents()) {
            switch (event.getEventType()) {
                case ADD_STOCK -> {
                    var eventAddStock = mapToAddStock(event);
                    if (product.equals(eventAddStock.getProduct())) {
                        totalQuantity += eventAddStock.getQuantity();
                    }
                }
                case CHECKOUT -> {
                    var shoppingCart = mapToShoppingCart(event);
                    for (var lineItem : shoppingCart.getLineItems()) {
                        if (product.equals(lineItem.getProduct())) {
                            totalQuantity -= lineItem.getQuantity()
                                * stocksConfig.getProductQuantity(lineItem.getProduct());
                        }
                    }
                }
            }
        }
        return totalQuantity;
    }
}
