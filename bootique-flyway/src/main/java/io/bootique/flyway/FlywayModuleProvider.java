package io.bootique.flyway;

import com.google.inject.Module;
import io.bootique.BQModuleProvider;

public class FlywayModuleProvider implements BQModuleProvider {

    @Override
    public Module module() {
        return new FlywayModule();
    }
}
