package io.baris.coffeeshop.inventory;

import io.baris.coffeeshop.checkout.model.LineItem;
import io.baris.coffeeshop.checkout.model.ShoppingCart;
import io.baris.coffeeshop.event.EventManager;
import io.baris.coffeeshop.inventory.model.InventoryProduct;
import io.baris.coffeeshop.product.model.ProductUnit;
import io.baris.coffeeshop.stock.model.AddStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

import static io.baris.coffeeshop.event.EventMapper.mapToAddStock;
import static io.baris.coffeeshop.event.EventMapper.mapToShoppingCart;

@Slf4j
@RequiredArgsConstructor
public class InventoryManager {

    private final EventManager eventManager;
    private final Jdbi jdbi;

    public void updateInventory(final ShoppingCart shoppingCart) {
        shoppingCart.getLineItems().stream()
            .map(LineItem::getProduct)
            .forEach(this::updateTotalQuantity);
    }

    public void updateInventory(final AddStock addStock) {
        updateTotalQuantity(addStock.getProduct());
    }

    private void updateTotalQuantity(String product) {
        var totalQuantity = calculateTotalQuantity(product);

        var inventoryProduct = InventoryProduct.builder()
            .product(product)
            .quantity(totalQuantity)
            .unit(ProductUnit.getProductUnit(product))
            .build();
        jdbi.withExtension(InventoryRepository.class, dao ->
            dao.updateProduct(inventoryProduct));
    }

    private int calculateTotalQuantity(String product) {
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
                            totalQuantity -= lineItem.inventoryQuantity();
                        }
                    }
                }
            }
        }
        return totalQuantity;
    }
}