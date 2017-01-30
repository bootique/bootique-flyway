package io.bootique.flyway;

import com.google.inject.Module;
import io.bootique.BQModuleProvider;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class FlywayModuleProvider implements BQModuleProvider {

    @Override
    public Module module() {
        return new FlywayModule();
    }

    @Override
    public Map<String, Type> configs() {
        return Collections.singletonMap("flyway", FlywayFactory.class);
    }
}
