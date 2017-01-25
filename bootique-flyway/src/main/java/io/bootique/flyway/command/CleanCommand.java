package io.bootique.flyway.command;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.flyway.FlywayRunner;
import io.bootique.meta.application.CommandMetadata;

import static io.bootique.flyway.command.FlywayCommand.command;

public class CleanCommand extends CommandWithMetadata {
    private Provider<FlywayRunner> runnerProvider;

    @Inject
    public CleanCommand(Provider<FlywayRunner> runnerProvider) {
        super(CommandMetadata
                .builder(CleanCommand.class)
                .description("Drops all objects (tables, views, procedures, triggers, ...) in the configured schemas." +
                        "The schemas are cleaned in the order specified by the schemas property.")
                .build());
        this.runnerProvider = runnerProvider;
    }

    @Override
    public CommandOutcome run(Cli cli) {
        return command(() -> runnerProvider.get().clean());
    }
}
