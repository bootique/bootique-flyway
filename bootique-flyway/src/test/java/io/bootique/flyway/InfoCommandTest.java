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

import io.bootique.command.CommandOutcome;
import io.bootique.jdbc.junit5.tc.TcDbTester;
import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestFactory;
import io.bootique.junit5.BQTestTool;
import io.bootique.junit5.TestIO;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

@BQTest
public class InfoCommandTest {

    @BQTestTool
    static final TcDbTester db = TcDbTester.db("jdbc:tc:postgresql:12:///info");

    @BQTestTool
    final BQTestFactory testFactory = new BQTestFactory();

    private String runCommand(String command) {

        TestIO io = TestIO.noTrace();

        CommandOutcome result = testFactory
                .app("--config", "classpath:io/bootique/flyway/InfoCommandTest.yml", command)
                .module(db.moduleWithTestDataSource("pg"))
                .autoLoadModules()
                .bootLogger(io.getBootLogger())
                .run();

        assertTrue(result.isSuccess());

        return io.getStdout();
    }

    @Test
    public void info() {

        runCommand("--clean");
        String afterCleanLog = runCommand("--info");
        assertLogContains(afterCleanLog,
                "^.*| Category  | Version | Description  | Type | Installed On | State   |$",
                "^.*| Versioned | 1       | Init         | SQL  |              | Pending |$",
                "^.*| Versioned | 2       | Update table | JDBC |              | Pending |$");

        runCommand("--migrate");
        String afterMigrateLog = runCommand("--info");
        assertLogContains(afterMigrateLog,
                "^.*| Category  | Version | Description  | Type | Installed On        | State   |$",
                "^.*| Versioned | 1       | Init         | SQL  | .......... ........ | Success |$",
                "^.*| Versioned | 2       | Update table | JDBC | .......... ........ | Success |$");
    }

    private void assertLogContains(String log, String... expectedPatterns) {

        String[] logLines = log.split("\\r?\\n");

        for (String pattern : expectedPatterns) {

            Pattern p = Pattern.compile(pattern);
            boolean matched = false;

            for (String maybeMatch : logLines) {
                if (p.matcher(maybeMatch).matches()) {
                    matched = true;
                    break;
                }
            }

            assertTrue(matched, () -> "Pattern not found: " + pattern);
        }
    }
}
