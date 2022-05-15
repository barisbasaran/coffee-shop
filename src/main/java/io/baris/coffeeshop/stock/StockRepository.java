package io.baris.coffeeshop.stock;

import io.baris.coffeeshop.stock.model.StockProduct;
import io.baris.coffeeshop.stock.model.ProductUnit;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Manages stocks in the database
 */
public interface StockRepository {

    Logger log = LoggerFactory.getLogger(StockRepository.class);

    @SqlQuery("SELECT * FROM stocks ORDER BY product")
    @RegisterBeanMapper(StockProduct.class)
    List<StockProduct> getStocks();

    @SqlUpdate("DELETE FROM stocks WHERE product = ?")
    void deleteProduct(String product);

    @SqlUpdate("INSERT INTO stocks (product, quantity, unit) VALUES (?, ?, ?)")
    void insertProduct(String product, int quantity, ProductUnit unit);

    @Transaction
    default StockProduct updateStocks(
        final StockProduct stockProduct
    ) {
        deleteProduct(stockProduct.getProduct());
        insertProduct(
            stockProduct.getProduct(),
            stockProduct.getQuantity(),
            stockProduct.getUnit()
        );
        log.info("Updated stocks with {}", stockProduct);
        return stockProduct;
    }
}
