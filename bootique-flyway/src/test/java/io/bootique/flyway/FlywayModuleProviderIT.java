package io.bootique.flyway;

import io.bootique.BQModuleProvider;
import org.junit.Test;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class FlywayModuleProviderIT {

    @Test
    public void testPresentInJar() {

        long c = StreamSupport
                .stream(ServiceLoader.load(BQModuleProvider.class).spliterator(), false)
                .filter(p -> p instanceof FlywayModuleProvider)
                .count();
        assertEquals("No provider found", 1, c);
    }
}
