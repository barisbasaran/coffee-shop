package io.baris.coffeeshop.testing;

import io.baris.coffeeshop.system.config.CoffeeShopConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import static io.baris.coffeeshop.system.utils.SystemUtils.readFileToString;

public class TestUtils {

    public static final String TEST_CONFIG = "classpath:test-config.yml";
    public static final int UNPROCESSIBLE_ENTITY = 422;

    public static CoffeeShopConfig loadConfig(final String path) {
        var representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);

        return new Yaml(representer)
            .loadAs(readFileToString(path), CoffeeShopConfig.class);
    }
}
