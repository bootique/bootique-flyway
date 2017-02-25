package io.bootique.flyway;

import com.google.inject.Binder;
import com.google.inject.Provides;
import io.bootique.BQCoreModule;
import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;
import io.bootique.flyway.command.BaselineCommand;
import io.bootique.flyway.command.CleanCommand;
import io.bootique.flyway.command.InfoCommand;
import io.bootique.flyway.command.MigrateCommand;
import io.bootique.flyway.command.RepairCommand;
import io.bootique.flyway.command.ValidateCommand;
import io.bootique.jdbc.DataSourceFactory;

import static java.util.Arrays.asList;

public class FlywayModule extends ConfigModule {

    @Override
    public void configure(Binder binder) {
        asList(
                BaselineCommand.class,
                CleanCommand.class,
                InfoCommand.class,
                MigrateCommand.class,
                RepairCommand.class,
                ValidateCommand.class
        ).forEach(command -> BQCoreModule.extend(binder).addCommand(command));
    }

    @Provides
    public FlywaySettings createFlywayDataSources(ConfigurationFactory configurationFactory,
                                                  DataSourceFactory dataSourceFactory) {
        return configurationFactory
                .config(FlywayFactory.class, configPrefix).createDataSources(dataSourceFactory);
    }

    @Provides
    public FlywayRunner createFlywayRunner(FlywaySettings flywaySettings) {
        return new FlywayRunner(flywaySettings);
    }
}
