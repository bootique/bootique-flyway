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

package io.bootique.flyway.command;

import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.flyway.Flyways;
import io.bootique.meta.application.CommandMetadata;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.internal.info.MigrationInfoDumper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfoCommand extends CommandWithMetadata {

    private final Provider<Flyways> flyways;

    @Inject
    public InfoCommand(Provider<Flyways> flyways) {
        super(CommandMetadata
                .builder(InfoCommand.class)
                .description("Prints the details and status information about all the migrations.")
                .build());
        this.flyways = flyways;
    }

    @Override
    public CommandOutcome run(Cli cli) {
        flyways.get().flyways().forEach(this::info);
        return CommandOutcome.succeeded();
    }

    private void info(Flyway f) {
        MigrationInfoService info = f.info();
        MigrationInfo current = info.current();
        MigrationVersion currentSchemaVersion = current == null ? MigrationVersion.EMPTY : current.getVersion();

        MigrationVersion schemaVersionToOutput = currentSchemaVersion == null ? MigrationVersion.EMPTY : currentSchemaVersion;

        final Logger logger = LoggerFactory.getLogger(Flyways.class);

        if (logger.isInfoEnabled()) {
            logger.info("Schema version: " + schemaVersionToOutput);
            logger.info("");

            for (String line : MigrationInfoDumper.dumpToAsciiTable(info.all()).split("\\r?\\n")) {
                logger.info(line);
            }
        }
    }
}
