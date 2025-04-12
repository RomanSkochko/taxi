package com.romanskochko.taxi;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class SharedRedisContainer {
    private static final GenericContainer<?> REDIS_CONTAINER;

    static {
        REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:latest"))
                .withExposedPorts(6379);
        REDIS_CONTAINER.start();
    }

    public static GenericContainer<?> getInstance() {
        return REDIS_CONTAINER;
    }


    @AfterAll
    public static void stopContainer() {
        REDIS_CONTAINER.stop();
    }
}
