package io.bootique.flyway;

import io.bootique.test.junit.BQModuleProviderChecker;
import org.junit.Test;

public class FlywayModuleProviderIT {

    @Test
    public void testPresentInJar() {
        BQModuleProviderChecker.testPresentInJar(FlywayModuleProvider.class);
    }
}
