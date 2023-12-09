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

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@BQConfig("Configures Flyway.")
public class FlywayFactory {

    private final DataSourceFactory dataSourceFactory;

    private List<String> dataSources = new ArrayList<>();
    private List<String> locations = Collections.singletonList("db/migration");
    private List<String> configFiles = new ArrayList<>(); // list of config files to use

    @Inject
    public FlywayFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    FlywaySettings create() {
        List<DataSource> dataSources = this.dataSources.stream().map(dataSourceFactory::forName).collect(Collectors.toList());
        return new FlywaySettings(dataSources, locations, configFiles);
    }

    @BQConfigProperty("References to dataSources to access the database.")
    public void setDataSources(List<String> dataSources) {
        this.dataSources = dataSources;
    }

    @BQConfigProperty("The locations to scan recursively for migration scripts.")
    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    @BQConfigProperty("The list of Flyway configuration files to use.")
    public void setConfigFiles(List<String> configFiles) {
        this.configFiles = configFiles;
    }
}
