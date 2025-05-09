/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class V2__Update_table extends BaseJavaMigration {

    private static Logger LOGGER = LoggerFactory.getLogger(V2__Update_table.class);

    public void migrate(Context context) {
        String insertSQL = "INSERT INTO test (id, name) VALUES (2, 'Test 2')";

        try (PreparedStatement statement = context.getConnection().prepareStatement(insertSQL)) {
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Migration failed", e);
            throw new RuntimeException(e);
        }
    }
}
