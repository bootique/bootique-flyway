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
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

/**
 * Tests for info command.
 *
 * @author Gert-Jan Paulissen
 */
public class InfoTest {

    @Rule
    public final BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void verifyInfoMessage() throws IOException {
        BQRuntime runtime;

        testFactory
                .app("--config=classpath:io/bootique/flyway/verifyInfoMessage.yml", "--clean")
                .autoLoadModules()
                .run();

        runtime = testFactory
                .app("--config=classpath:io/bootique/flyway/verifyInfoMessage.yml", "--info")
                .autoLoadModules()
                .createRuntime();

        assertTrue(runtime.run().isSuccess());
        assertLogFileContents(
                "^.*| Category  | Version | Description  | Type | Installed On | State   |$",
                "^.*| Versioned | 1       | Init         | SQL  |              | Pending |$",
                "^.*| Versioned | 2       | Update table | JDBC |              | Pending |$");

        testFactory
                .app("--config=classpath:io/bootique/flyway/verifyInfoMessage.yml", "--migrate")
                .autoLoadModules()
                .run();

        runtime = testFactory
                .app("--config=classpath:io/bootique/flyway/verifyInfoMessage.yml", "--info")
                .autoLoadModules()
                .createRuntime();

        assertTrue(runtime.run().isSuccess());
        assertLogFileContents(
                "^.*| Category  | Version | Description  | Type | Installed On        | State   |$",
                "^.*| Versioned | 1       | Init         | SQL  | .......... ........ | Success |$",
                "^.*| Versioned | 2       | Update table | JDBC | .......... ........ | Success |$");
    }

    private void assertLogFileContents(String... patterns) throws IOException {

        File file = new File("target/test.log");
        assertTrue("No test log file", file.exists());
        List<String> actualLines = Files.readAllLines(file.toPath());

        for (String pattern : patterns) {

            Pattern p = Pattern.compile(pattern);
            boolean matched = false;

            for (String maybeMatch : actualLines) {
                if (p.matcher(maybeMatch).matches()) {
                    matched = true;
                    break;
                }
            }

            assertTrue("Pattern not found: " + pattern, matched);
        }
    }
}
