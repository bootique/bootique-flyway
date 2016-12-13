package io.bootique.flyway;

import io.bootique.jdbc.DataSourceFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FlywayFactory {
    private List<String> dataSources = new ArrayList<>();
    private List<String> locations = Collections.singletonList("db/migration");

    FlywaySettings createDataSources(DataSourceFactory dataSourceFactory) {
        final List<DataSource> dataSources = this.dataSources.stream().map(dataSourceFactory::forName).collect(Collectors.toList());
        return new FlywaySettings(dataSources, locations);
    }

    public FlywayFactory setDataSources(List<String> dataSources) {
        this.dataSources = dataSources;
        return this;
    }

    public FlywayFactory setLocations(List<String> locations) {
        this.locations = locations;
        return this;
    }
}
