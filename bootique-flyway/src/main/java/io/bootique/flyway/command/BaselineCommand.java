package io.bootique.flyway.command;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.flyway.FlywayRunner;
import io.bootique.meta.application.CommandMetadata;

import static io.bootique.flyway.command.FlywayCommand.command;

public class BaselineCommand extends CommandWithMetadata {
    private Provider<FlywayRunner> runnerProvider;

    @Inject
    public BaselineCommand(Provider<FlywayRunner> runnerProvider) {
        super(CommandMetadata
                .builder(BaselineCommand.class)
                .description("Baselines an existing database, " +
                        "excluding all migrations up to and including baselineVersion.")
                .build());
        this.runnerProvider = runnerProvider;
    }

    @Override
    public CommandOutcome run(Cli cli) {
        return command(() -> runnerProvider.get().baseline());
    }
}
