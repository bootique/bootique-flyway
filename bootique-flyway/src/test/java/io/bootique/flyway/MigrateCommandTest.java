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

package io.bootique.flyway;

import io.bootique.BQRuntime;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.jdbc.junit5.Table;
import io.bootique.jdbc.junit5.connector.DbConnector;
import io.bootique.jdbc.junit5.metadata.DbMetadata;
import io.bootique.jdbc.junit5.tc.TcDbTester;
import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestFactory;
import io.bootique.junit5.BQTestTool;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@BQTest
public class MigrateCommandTest {

    @BQTestTool
    final TcDbTester db = TcDbTester.db("jdbc:tc:postgresql:11:///migrate");

    @BQTestTool
    final BQTestFactory testFactory = new BQTestFactory().autoLoadModules();

    @ParameterizedTest
    @ValueSource(strings = {
            "classpath:io/bootique/flyway/defaultMigration.yml",
            "classpath:io/bootique/flyway/explicitDefaultMigration.yml",
            "classpath:io/bootique/flyway/explicitNonDefaultMigration.yml",
            "classpath:io/bootique/flyway/explicitNonDefaultMigrationConfigfile.yml"
    })
    public void migration(String config) {
        BQRuntime app = testFactory
                .app("--config", config, "--migrate")
                .module(db.moduleWithTestDataSource("pg"))
                .createRuntime();

        assertTrue(app.run().isSuccess());

        DataSource ds = app.getInstance(DataSourceFactory.class).forName("pg");
        DbConnector connector = new DbConnector(ds, DbMetadata.create(ds));
        Table a = connector.getTable("test");

        a.matcher().assertMatches(2);
        a.matcher().eq("id", 1).andEq("name", "Test").assertOneMatch();
        a.matcher().eq("id", 2).andEq("name", "Test 2").assertOneMatch();
    }
}
