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

import io.bootique.BQCoreModule;
import io.bootique.BQModuleProvider;
import io.bootique.bootstrap.BuiltModule;
import io.bootique.config.ConfigurationFactory;
import io.bootique.di.BQModule;
import io.bootique.di.Binder;
import io.bootique.di.Provides;
import io.bootique.flyway.command.*;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.jdbc.JdbcModule;

import java.util.Collection;
import java.util.Collections;

import static java.util.Arrays.asList;

public class FlywayModule implements BQModule, BQModuleProvider {

    private static final String CONFIG_PREFIX = "flyway";

    @Override
    public BuiltModule buildModule() {
        return BuiltModule.of(this)
                .provider(this)
                .description("Integrates Flyway database migrations library")
                .config(CONFIG_PREFIX, FlywayFactory.class)
                .build();
    }

    @Override
    @Deprecated(since = "3.0", forRemoval = true)
    public Collection<BQModuleProvider> dependencies() {
        return Collections.singletonList(new JdbcModule());
    }

    @Override
    public void configure(Binder binder) {
        asList(
                BaselineCommand.class,
                CleanCommand.class,
                InfoCommand.class,
                MigrateCommand.class,
                RepairCommand.class,
                ValidateCommand.class
        ).forEach(command -> BQCoreModule.extend(binder).addCommand(command));
    }

    @Provides
    public FlywaySettings createFlywayDataSources(ConfigurationFactory configFactory, DataSourceFactory dataSourceFactory) {
        return configFactory.config(FlywayFactory.class, CONFIG_PREFIX).createDataSources(dataSourceFactory);
    }

    @Provides
    public FlywayRunner createFlywayRunner(FlywaySettings flywaySettings) {
        return new FlywayRunner(flywaySettings);
    }
}
