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

[![build test deploy](https://github.com/bootique/bootique-flyway/actions/workflows/maven.yml/badge.svg)](https://github.com/bootique/bootique-flyway/actions/workflows/maven.yml)
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
            <version>3.0-M4</version>
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

### FLYWAY COMMANDS AS OPTIONS

Flyway is based around just 6 commands: Migrate, Clean, Info, Validate, Baseline and Repair. They are represented in 
the Bootique-Flyway module as command line options.

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

The standard Bootique command line options you may need:


```
  -c yaml_location, --config=yaml_location
           Specifies YAML config location, which can be a classpath (prefixed by classpath:), file path or a URL.

  -H, --help-config
           Prints information about application modules and their configuration
           options.
```

## Configuration

### YAML configuration file

As an example here a YAML test config file: classpath:io/bootique/flyway/explicitNonDefaultMigrationConfigfile.yml.


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

In this example the locations property (with bogus as a non-existent location)
is superseded by the settings in the Flyway configuration file.

### Flyway configuration file

As an example here a Flyway test configuration file: classpath:io/bootique/flyway/explicitNonDefaultMigrationConfigfile.conf.


```
flyway.locations = path/migration
```

## Example Project

[bootique-flyway-demo](https://github.com/bootique-examples/bootique-flyway-demo)
