package io.bootique.flyway;

import com.google.inject.Module;
import io.bootique.BQModuleProvider;
import io.bootique.jdbc.JdbcModuleProvider;

import java.lang.reflect.Type;
import java.util.Collection;
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

    @Override
    public Collection<BQModuleProvider> dependencies() {
        return Collections.singletonList(new JdbcModuleProvider());
    }
}
