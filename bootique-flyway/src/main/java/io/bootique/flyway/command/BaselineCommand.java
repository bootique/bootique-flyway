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

public class BaselineCommand extends CommandWithMetadata {

    private final Provider<Flyways> flyways;

    @Inject
    public BaselineCommand(Provider<Flyways> flyways) {
        super(CommandMetadata
                .builder(BaselineCommand.class)
                .description("Baselines an existing database, " +
                        "excluding all migrations up to and including baselineVersion.")
                .build());
        this.flyways = flyways;
    }

    @Override
    public CommandOutcome run(Cli cli) {
        flyways.get().flyways().forEach(Flyway::baseline);
        return CommandOutcome.succeeded();
    }
}
