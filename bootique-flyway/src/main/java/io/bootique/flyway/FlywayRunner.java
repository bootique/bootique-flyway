package io.bootique.flyway;

import org.flywaydb.core.Flyway;

import java.util.function.Consumer;

public class FlywayRunner {
    private final FlywaySettings settings;

    public FlywayRunner(FlywaySettings settings) {
        this.settings = settings;
    }

    public void migrate() {
        forEach(Flyway::migrate);
    }

    public void validate() {
        forEach(Flyway::validate);
    }

    public void baseline() {
        forEach(Flyway::baseline);
    }

    public void repair() {
        forEach(Flyway::repair);
    }

    public void info() {
        forEach(Flyway::info);
    }

    public void clean() {
        forEach(Flyway::clean);
    }

    private void forEach(Consumer<Flyway> flywayConsumer) {
        settings.getDataSources().forEach(ds -> {
            Flyway flyway = new Flyway();
            flyway.setLocations(settings.getLocations());
            flyway.setDataSource(ds);
            flywayConsumer.accept(flyway);
        });
    }
}
