package io.baris.coffeeshop.testing;

import io.baris.coffeeshop.cqrs.event.EventManager;
import io.baris.coffeeshop.cqrs.event.model.Event;
import lombok.Getter;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

/**
 * Junit rule to start PostgreSQL Docker container
 */
public class PostgreExtension implements BeforeAllCallback, AfterAllCallback {

    private final PostgreSQLContainer container;

    @Getter
    private Jdbi jdbi;

    private EventManager eventManager;

    public PostgreExtension(final String configPath) {
        this.container = loadPostgreDockerContainer(configPath);
        this.container.start();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        container.stop();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        this.jdbi = Jdbi.create(
            container.getJdbcUrl(),
            container.getUsername(),
            container.getPassword()
        );
        this.jdbi.installPlugin(new SqlObjectPlugin());

        this.eventManager = new EventManager(jdbi);
    }

    public String getDatabaseUrl() {
        return container.getJdbcUrl();
    }

    public List<Event> getAllEvents() {
        return eventManager.getAllEvents();
    }

    private PostgreSQLContainer loadPostgreDockerContainer(
        final String configPath
    ) {
        var configuration = TestUtils.loadConfig(configPath);
        var database = configuration.getDatabase();
        var databaseConfig = configuration.getDatabaseConfig();

        try (var postgreSQLContainer =
                 new PostgreSQLContainer(databaseConfig.getDockerImage())
                     .withUsername(database.getUser())
                     .withPassword(database.getPassword())
                     .withDatabaseName(databaseConfig.getName())
        ) {
            return postgreSQLContainer;
        }
    }
}
