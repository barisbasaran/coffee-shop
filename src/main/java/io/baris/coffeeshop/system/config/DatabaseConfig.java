package io.baris.coffeeshop.system.config;

import lombok.Data;

@Data
public class DatabaseConfig {

    private String name;
    private String dockerImage;
    private String initScript;
}
