package io.baris.coffeeshop.system.config;

import lombok.Value;

@Value
public class DatabaseConfig {

    String name;
    String dockerImage;
    String initScript;
}
