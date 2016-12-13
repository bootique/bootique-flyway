package io.bootique.flyway.command;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.bootique.application.CommandMetadata;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.flyway.FlywayRunner;

import static io.bootique.flyway.command.FlywayCommand.command;

public class MigrateCommand extends CommandWithMetadata {
    private Provider<FlywayRunner> runnerProvider;

    @Inject
    public MigrateCommand(Provider<FlywayRunner> runnerProvider) {
        super(CommandMetadata
                .builder(MigrateCommand.class)
                .description("Migrates the schema to the latest version. " +
                        "Flyway will create the metadata table automatically if it doesn't exist.")
                .build());
        this.runnerProvider = runnerProvider;
    }

    @Override
    public CommandOutcome run(Cli cli) {
        return command(() -> runnerProvider.get().migrate());
    }
}
