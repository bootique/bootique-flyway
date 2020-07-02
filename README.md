<!--
  Licensed to ObjectStyle LLC under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ObjectStyle LLC licenses
  this file to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
  -->

[![Build Status](https://travis-ci.org/bootique/bootique-flyway.svg)](https://travis-ci.org/bootique/bootique-flyway)
[![Maven Central](https://img.shields.io/maven-central/v/io.bootique.flyway/bootique-flyway.svg?colorB=brightgreen)](https://search.maven.org/artifact/io.bootique.flyway/bootique-flyway/)

# bootique-flyway

Provides [Flyway](https://flywaydb.org/) migrations framework integration with [Bootique](http://bootique.io).
See usage example [bootique-flyway-demo](https://github.com/bootique-examples/bootique-flyway-demo).

# Setup

## Add bootique-flyway to your build tool:

**Maven**
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.bootique.bom</groupId>
            <artifactId>bootique-bom</artifactId>
            <version>1.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependency>
    <groupId>io.bootique.flyway</groupId>
    <artifactId>bootique-flyway</artifactId>
</dependency>
```

**Gradle**
```groovy
compile("io.bootique.flyway:bootique-flyway:1.0")
```

*Note:* **bootique-flyway** is a part of [bootique-bom](https://github.com/bootique/bootique-bom), and version can be 
imported from there.


## Available commands

Flyway is based around just 6 commands: Migrate, Clean, Info, Validate, Baseline and Repair. They are presented in 
Bootique-Flyway module.

### FLYWAY OPTIONS

```
  -b, --baseline
       Baselines an existing database, excluding all migrations up to and including baselineVersion.

  --clean
       Drops all objects (tables, views, procedures, triggers, ...) in the configured schemas.The schemas are cleaned in the order specified by the schemas property.

  -i, --info
       Prints the details and status information about all the migrations.

  -m, --migrate
       Migrates the schema to the latest version. Flyway will create the metadata table automatically if it doesn't exist.

  -r, --repair
       Repairs the metadata table.

  -v, --validate
       Validate applied migrations against resolved ones (on the filesystem or classpath) to detect accidental changes that may prevent the schema(s) from being recreated exactly.
```

### BOOTIQUE OPTIONS

```
  -c yaml_location, --config=yaml_location
           Specifies YAML config location, which can be a classpath (prefixed by classpath:), file path or a URL.

  -H, --help-config
           Prints information about application modules and their configuration
           options.
```

## Configuration

### YAML file

You may specify classpath:io/bootique/flyway/explicitNonDefaultMigrationConfigfile.yml as the YAML config file:


```
jdbc:
  test:
    url: jdbc:h2:mem:defaultMigration
    username: bogus
    password: bogus
    driverClassName: org.h2.Driver

flyway:
  locations:
    - bogus
  configFiles:
    - classpath:io/bootique/flyway/explicitNonDefaultMigrationConfigfile.conf
  dataSources:
    - test
```

Only three flyway options are recognized:
- locations: a list of Flyway locations to look for Flyway migrations
- configFiles: a list of Flyway configuraton files (as custom config files, see https://flywaydb.org/documentation/commandline/)
- dataSources: a list of JDBC connections

Using custom config files allows you to more easily run Flyway using a file
for database connection settings (the Bootique YAML file) and application
settings in a Flyway file.

### Flyway file

You may use classpath:io/bootique/flyway/explicitNonDefaultMigrationConfigfile.conf as the Flyway configuration file.


```
flyway.locations = path/migration
```

## Example Project

[bootique-flyway-demo](https://github.com/bootique-examples/bootique-flyway-demo)
