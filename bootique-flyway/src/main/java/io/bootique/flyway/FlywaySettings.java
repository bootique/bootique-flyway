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

import io.bootique.resource.ResourceFactory;

import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.sql.DataSource;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.internal.configuration.ConfigUtils;
import org.flywaydb.core.internal.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlywaySettings {
    private static Logger logger = LoggerFactory.getLogger(FlywayRunner.class);

    private final List<DataSource> dataSources;
    private final String[] locations;
    private final String[] configFiles; // list of config files to use

    public FlywaySettings(List<DataSource> dataSources,
                          List<String> locations,
                          List<String> configFiles) {
        this.dataSources = Collections.unmodifiableList(dataSources);
        this.locations = locations.toArray(new String[0]);
        this.configFiles = configFiles.toArray(new String[0]);
    }

    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public String[] getLocations() {
        return locations;
    }

    public java.util.Map<java.lang.String,java.lang.String> getProperties() {
        Map<String, String> config = new HashMap<String, String>();

        for (String file : this.configFiles) {
            final String errorMessage = "Unable to load config file: " + file;

            try {
                final URL url = new ResourceFactory(file).getUrl(); // file may have classpath: as a prefix
                final Reader reader = new InputStreamReader(url.openStream());
                
                config.putAll(ConfigUtils.loadConfigurationFromReader(reader));
            } catch(IOException e) {
                throw new FlywayException(errorMessage, e);
            }
        }

        dumpConfiguration(config);

        return config;
    }

    /**
     * Dumps the configuration to the console when debug output is activated.
     *
     * @param config The configured properties.
     */
    private static void dumpConfiguration(Map<String, String> config) {
        if (logger.isDebugEnabled()) {
            logger.debug("Using configuration:");
            for (Map.Entry<String, String> entry : new TreeMap<>(config).entrySet()) {
                logger.debug(entry.getKey() + " -> " + entry.getValue());
            }
        }
    }
}
