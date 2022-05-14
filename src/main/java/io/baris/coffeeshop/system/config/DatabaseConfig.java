package io.baris.coffeeshop.system.config;

import lombok.Data;

@Data
public class DatabaseConfig {

    String name;
    String dockerImage;
    String initScript;
}
