package io.baris.coffeeshop.system;

import org.jdbi.v3.core.Jdbi;

import java.util.Arrays;

/**
 * Utilities for Postgre database
 */
public class PostgreUtils {

    public static void applySqlScript(final Jdbi jdbi, final String path) {
        String tables = SystemUtils.readFileToString(path);
        jdbi.withHandle(handle -> {
            Arrays.stream(tables.split(";")).forEach(handle::execute);
            return 1;
        });
    }
}
