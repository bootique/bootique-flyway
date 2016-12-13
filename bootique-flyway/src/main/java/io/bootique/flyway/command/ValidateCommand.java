package io.bootique.flyway.command;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.bootique.application.CommandMetadata;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.flyway.FlywayRunner;

import static io.bootique.flyway.command.FlywayCommand.command;

public class ValidateCommand extends CommandWithMetadata {
    private Provider<FlywayRunner> runnerProvider;

    @Inject
    public ValidateCommand(Provider<FlywayRunner> runnerProvider) {
        super(CommandMetadata
                .builder(ValidateCommand.class)
                .description("Validate applied migrations against resolved ones " +
                        "(on the filesystem or classpath) to detect accidental " +
                        "changes that may prevent the schema(s) from being recreated exactly.")
                .build());
        this.runnerProvider = runnerProvider;
    }

    @Override
    public CommandOutcome run(Cli cli) {
        return command(() -> runnerProvider.get().validate());
    }
}
