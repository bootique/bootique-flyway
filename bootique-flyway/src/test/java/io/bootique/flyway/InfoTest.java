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
import io.bootique.command.CommandOutcome;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

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
    public void verifyInfoMessage() {
        BQRuntime runtime;
        
        testFactory
            .app("--config=classpath:io/bootique/flyway/verifyInfoMessage.yml", "--clean")
            .autoLoadModules()
            .run();
        
        runtime = testFactory
            .app("--config=classpath:io/bootique/flyway/verifyInfoMessage.yml", "--info")
            .autoLoadModules()
            .createRuntime();

        testInfoCommand(runtime, 1);
        
        testFactory
            .app("--config=classpath:io/bootique/flyway/verifyInfoMessage.yml", "--migrate")
            .autoLoadModules()
            .run();

        runtime = testFactory
            .app("--config=classpath:io/bootique/flyway/verifyInfoMessage.yml", "--info")
            .autoLoadModules()
            .createRuntime();

        testInfoCommand(runtime, 2);
    }
    
    private void testInfoCommand(BQRuntime runtime, int which) {
        CommandOutcome result = runtime.run();
        assertTrue(result.isSuccess());

        if (which == 1) {
            assertTrue(textFoundInLogFile("+-----------+---------+--------------+------+--------------+---------+"));
            assertTrue(textFoundInLogFile("| Category  | Version | Description  | Type | Installed On | State   |"));
            assertTrue(textFoundInLogFile("+-----------+---------+--------------+------+--------------+---------+"));
            assertTrue(textFoundInLogFile("| Versioned | 1       | Init         | SQL  |              | Pending |"));
            assertTrue(textFoundInLogFile("| Versioned | 2       | Update table | JDBC |              | Pending |"));
            assertTrue(textFoundInLogFile("+-----------+---------+--------------+------+--------------+---------+"));
        } else {
            assertTrue(textFoundInLogFile("+-----------+---------+--------------+------+---------------------+---------+"));
            assertTrue(textFoundInLogFile("| Category  | Version | Description  | Type | Installed On        | State   |"));
            assertTrue(textFoundInLogFile("+-----------+---------+--------------+------+---------------------+---------+"));
            assertTrue(textFoundInLogFile("| Versioned | 1       | Init         | SQL  | .......... ........ | Success |"));
            assertTrue(textFoundInLogFile("| Versioned | 2       | Update table | JDBC | .......... ........ | Success |"));
            assertTrue(textFoundInLogFile("+-----------+---------+--------------+------+---------------------+---------+"));
        }
    }

    private boolean textFoundInLogFile(String text) {
        try {
            Scanner scanner = new Scanner(new File("target/test.log"));
            String pattern = text.replace("+", "\\+").replace("|", "\\|");

            //now read the file line by line...
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().matches("^.*" + pattern + "$"))
                    {
                        return true;
                    }
            }

        } catch(FileNotFoundException e) { 
            return false;
        }
        
        return false;
    }
}
