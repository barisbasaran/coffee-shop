package io.baris.coffeeshop.testing;

import io.baris.coffeeshop.system.CoffeeShopConfiguration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import static io.baris.coffeeshop.system.SystemUtils.readFileToString;

public class TestUtils {

    public static final String TEST_CONFIG = "classpath:test-config.yml";
    public static final int UNPROCESSIBLE_ENTITY = 422;

    public static CoffeeShopConfiguration loadConfig(final String path) {
        var representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);

        return new Yaml(representer)
            .loadAs(readFileToString(path), CoffeeShopConfiguration.class);
    }
}
