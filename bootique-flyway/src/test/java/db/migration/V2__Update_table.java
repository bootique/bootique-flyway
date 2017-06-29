package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class V2__Update_table implements JdbcMigration {
    private static Logger LOGGER = LoggerFactory.getLogger(V2__Update_table.class);

    @Override
    public void migrate(Connection connection) {
        String insertSQL = "INSERT INTO TEST (id, name) VALUES (2, 'Test 2')";

        try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Migration failed", e);
            throw new RuntimeException(e);
        }
    }
}