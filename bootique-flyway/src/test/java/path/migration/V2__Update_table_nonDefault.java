package path.migration;

import db.migration.V2__Update_table;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;

public class V2__Update_table_nonDefault implements JdbcMigration {

    @Override
    public void migrate(Connection connection) {
        new V2__Update_table().migrate(connection);
    }
}