package io.bootique.flyway.command;

import com.google.inject.Inject;
import com.google.inject.Provider;
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
