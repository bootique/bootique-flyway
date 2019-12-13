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

import javax.inject.Inject;
import javax.inject.Provider;

import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.flyway.FlywayRunner;
import io.bootique.meta.application.CommandMetadata;

import static io.bootique.flyway.command.FlywayCommand.command;

public class RepairCommand extends CommandWithMetadata {
    private Provider<FlywayRunner> runnerProvider;

    @Inject
    public RepairCommand(Provider<FlywayRunner> runnerProvider) {
        super(CommandMetadata
                .builder(RepairCommand.class)
                .description("Repairs the metadata table.")
                .build());
        this.runnerProvider = runnerProvider;
    }

    @Override
    public CommandOutcome run(Cli cli) {
        return command(() -> runnerProvider.get().repair());
    }
}
