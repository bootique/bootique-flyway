package io.bootique.flyway;

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.jdbc.DataSourceFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@BQConfig("Configures Flyway.")
public class FlywayFactory {
    private List<String> dataSources = new ArrayList<>();
    private List<String> locations = Collections.singletonList("db/migration");

    FlywaySettings createDataSources(DataSourceFactory dataSourceFactory) {
        final List<DataSource> dataSources = this.dataSources.stream().map(dataSourceFactory::forName).collect(Collectors.toList());
        return new FlywaySettings(dataSources, locations);
    }

    @BQConfigProperty("References to dataSources to access the database.")
    public void setDataSources(List<String> dataSources) {
        this.dataSources = dataSources;
    }

    @BQConfigProperty("The locations to scan recursively for migration scripts.")
    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
