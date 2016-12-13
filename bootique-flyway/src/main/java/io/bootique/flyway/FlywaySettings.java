package io.bootique.flyway;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

public class FlywaySettings {
    private final List<DataSource> dataSources;
    private final String[] locations;

    public FlywaySettings(List<DataSource> dataSources, List<String> locations) {
        this.dataSources = Collections.unmodifiableList(dataSources);
        this.locations = locations.toArray(new String[locations.size()]);
    }

    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public String[] getLocations() {
        return locations;
    }
}
