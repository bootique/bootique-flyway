/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.bootique.flyway;

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.resource.ResourceFactory;
import jakarta.inject.Inject;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.configuration.ConfigUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@BQConfig("Configures Flyway.")
public class FlywayFactory {

    private final DataSourceFactory dataSourceFactory;

    private List<String> dataSources;
    private List<String> locations;
    private List<String> configFiles;

    @Inject
    public FlywayFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    public Flyways create() {

        String[] locations = this.locations != null ? this.locations.toArray(new String[0]) : new String[]{"db/migration"};
        Map<String, String> configProperties = readConfigProperties();
        List<String> dsNames = this.dataSources != null ? this.dataSources : List.of();

        List<Flyway> flyways = dsNames
                .stream()
                .map(dataSourceFactory::forName)
                .map(ds -> createFlyway(ds, locations, configProperties))
                .collect(Collectors.toList());

        return new Flyways(flyways);
    }

    private Flyway createFlyway(DataSource dataSource, String[] locations, Map<String, String> configProperties) {
        Configuration config = Flyway.configure()
                .locations(locations)
                .dataSource(dataSource)
                .configuration(configProperties);

        return new Flyway(config);
    }

    private Map<String, String> readConfigProperties() {

        if (this.configFiles == null || configFiles.isEmpty()) {
            return Map.of();
        }

        Map<String, String> properties = new HashMap<>();

        for (String file : this.configFiles) {
            URL url = new ResourceFactory(file).getUrl();

            try (Reader reader = new InputStreamReader(url.openStream())) {
                properties.putAll(ConfigUtils.loadConfigurationFromReader(reader));
            } catch (IOException e) {
                throw new RuntimeException("Error reading file: " + file, e);
            }
        }

        return properties;
    }

    @BQConfigProperty("References to dataSources to access the database.")
    public void setDataSources(List<String> dataSources) {
        this.dataSources = dataSources;
    }

    @BQConfigProperty("The locations to scan recursively for migration scripts. If missing, \"db/migration\" path is assumed")
    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    @BQConfigProperty("The list of Flyway configuration files to use.")
    public void setConfigFiles(List<String> configFiles) {
        this.configFiles = configFiles;
    }
}
