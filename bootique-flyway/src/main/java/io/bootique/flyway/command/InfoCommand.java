package io.bootique.flyway.command;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.bootique.application.CommandMetadata;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.flyway.FlywayRunner;

import static io.bootique.flyway.command.FlywayCommand.command;

public class InfoCommand extends CommandWithMetadata {
    private Provider<FlywayRunner> runnerProvider;

    @Inject
    public InfoCommand(Provider<FlywayRunner> runnerProvider) {
        super(CommandMetadata
                .builder(InfoCommand.class)
                .description("Prints the details and status information about all the migrations.")
                .build());
        this.runnerProvider = runnerProvider;
    }

    @Override
    public CommandOutcome run(Cli cli) {
        return command(() -> runnerProvider.get().info());
    }
}
