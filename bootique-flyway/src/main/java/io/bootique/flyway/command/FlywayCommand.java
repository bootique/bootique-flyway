package io.bootique.flyway.command;

import io.bootique.command.CommandOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionalInterface
interface FlywayCommand {
    Logger LOGGER = LoggerFactory.getLogger(FlywayCommand.class);

    /**
     * SAM for passing operations inside command method.
     */
    void apply() throws Exception;

    static CommandOutcome command(FlywayCommand command) {
        try {
            command.apply();
            return CommandOutcome.succeeded();
        } catch (Exception e) {
            LOGGER.error("Error while executing command.", e);
            return CommandOutcome.failed(1, e.getMessage());
        }
    }
}
