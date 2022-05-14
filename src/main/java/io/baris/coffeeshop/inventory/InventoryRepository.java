package io.baris.coffeeshop.inventory;

import io.baris.coffeeshop.event.model.Event;
import io.baris.coffeeshop.inventory.model.InventoryProduct;
import io.baris.coffeeshop.product.model.ProductUnit;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Manages pets in the database
 */
public interface InventoryRepository {

    Logger log = LoggerFactory.getLogger(InventoryRepository.class);

    @SqlQuery("SELECT * FROM inventory ORDER BY product")
    @RegisterBeanMapper(Event.class)
    List<InventoryProduct> getProducts();

    @SqlUpdate("DELETE FROM inventory WHERE product = ?")
    void deleteProduct(String product);

    @SqlUpdate("INSERT INTO inventory (product, quantity, unit) VALUES (?, ?, ?)")
    void insertProduct(String product, int quantity, ProductUnit unit);

    @Transaction
    default InventoryProduct updateInventory(final InventoryProduct inventoryProduct) {
        deleteProduct(inventoryProduct.getProduct());
        insertProduct(
            inventoryProduct.getProduct(),
            inventoryProduct.getQuantity(),
            inventoryProduct.getUnit()
        );
        log.info("Updated inventory with {}", inventoryProduct);
        return inventoryProduct;
    }
}
