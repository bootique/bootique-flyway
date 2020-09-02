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

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.io.*;
import java.net.URL;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.internal.util.FileCopyUtils;
import org.flywaydb.core.internal.configuration.ConfigUtils;

public class FlywaySettings {
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

        try {
            for (String file : this.configFiles) {
                URL url = new ResourceFactory(file).getUrl(); // file may have classpath: as a prefix

                Reader reader = new InputStreamReader(url.openStream());
                
                // loadConfigurationFromReader() only available for Flyway 6
                // config.putAll(ConfigUtils.loadConfigurationFromReader(reader));

                String errorMessage = "Unable to load config file: " + file;

                try {
                    String contents = FileCopyUtils.copyToString(reader);
                    Properties properties = new Properties();
                    properties.load(new StringReader(contents.replace("\\", "\\\\")));
                    return ConfigUtils.propertiesToMap(properties);
                } catch (IOException e) {
                    throw new FlywayException(errorMessage, e);
                }
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        return config;
    }
}
