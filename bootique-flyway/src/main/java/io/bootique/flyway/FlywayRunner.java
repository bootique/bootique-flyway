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

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.internal.info.MigrationInfoDumper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class FlywayRunner {
    private static Logger logger = LoggerFactory.getLogger(FlywayRunner.class);

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
        settings.getDataSources().forEach(ds -> {
            Flyway flyway = new Flyway(Flyway.configure()
                    .locations(settings.getLocations())
                    .dataSource(ds)
                    .configuration(settings.getProperties()) // takes precedence over location settings (do not use jdbc connection details though in a Flyway configuration file)
            );

            MigrationInfoService info = flyway.info();
            MigrationInfo current = info.current();
            MigrationVersion currentSchemaVersion = current == null ? MigrationVersion.EMPTY : current.getVersion();

            MigrationVersion schemaVersionToOutput = currentSchemaVersion == null ? MigrationVersion.EMPTY : currentSchemaVersion;
           
            if(logger.isInfoEnabled()) {
                logger.info("Schema version: " + schemaVersionToOutput);
                logger.info("");

                for (String line : MigrationInfoDumper.dumpToAsciiTable(info.all()).split("\\r?\\n")) {
                    logger.info(line);
                }
            }
        });
    }

    public void clean() {
        forEach(Flyway::clean);
    }

    private void forEach(Consumer<Flyway> flywayConsumer) {
        settings.getDataSources().forEach(ds -> {
            Flyway flyway = new Flyway(Flyway.configure()
                    .locations(settings.getLocations())
                    .dataSource(ds)
                    .configuration(settings.getProperties()) // takes precedence over location settings (do not use jdbc connection details though in a Flyway configuration file)
            );
            
            flywayConsumer.accept(flyway);
        });
    }
}
