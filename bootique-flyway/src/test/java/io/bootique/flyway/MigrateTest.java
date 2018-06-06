/**
 *    Licensed to the ObjectStyle LLC under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ObjectStyle LLC licenses
 *  this file to you under the Apache License, Version 2.0 (the
 *  “License”); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.bootique.flyway;

import io.bootique.BQRuntime;
import io.bootique.command.CommandOutcome;
import io.bootique.jdbc.test.DatabaseChannel;
import io.bootique.jdbc.test.Table;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for migrate command.
 *
 * @author Ibragimov Ruslan
 */
public class MigrateTest {

    @Rule
    public final BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void defaultMigration() {
        BQRuntime runtime = testFactory
            .app("--config=classpath:io/bootique/flyway/defaultMigration.yml", "--migrate")
            .autoLoadModules()
            .createRuntime();

        testMigrateCommand(runtime);
    }

    @Test
    public void defaultExplicitMigration() {
        BQRuntime runtime = testFactory
            .app("--config=classpath:io/bootique/flyway/expilicitDefaultMigration.yml", "--migrate")
            .autoLoadModules()
            .createRuntime();

        testMigrateCommand(runtime);
    }

    @Test
    public void nonStandardLocationMigration() {
        BQRuntime runtime = testFactory
            .app("--config=classpath:io/bootique/flyway/expilicitDefaultMigration.yml", "--migrate")
            .autoLoadModules()
            .createRuntime();

        testMigrateCommand(runtime);
    }

    private void testMigrateCommand(BQRuntime runtime) {
        CommandOutcome result = runtime.run();
        assertTrue(result.isSuccess());

        Table a = DatabaseChannel.get(runtime).newTable("TEST").columnNames("ID", "NAME").build();
        List<Object[]> row = a.select();
        assertEquals(1, row.get(0)[0]);
        assertEquals("Test", row.get(0)[1]);
        assertEquals(2, row.get(1)[0]);
        assertEquals("Test 2", row.get(1)[1]);

        a.matcher().assertMatches(2);
    }
}
