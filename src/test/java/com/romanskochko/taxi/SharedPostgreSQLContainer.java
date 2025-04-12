package com.romanskochko.taxi;

import org.junit.jupiter.api.AfterAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class SharedPostgreSQLContainer {

    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;

    static {
        //noinspection resource
        POSTGRESQL_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"))
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
        POSTGRESQL_CONTAINER.start();
    }

    public static PostgreSQLContainer<?> getInstance() {
        return POSTGRESQL_CONTAINER;
    }

    @AfterAll
    public static void stopContainer() {
        POSTGRESQL_CONTAINER.stop();
    }
}

