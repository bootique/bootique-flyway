[![Build Status](https://travis-ci.org/bootique/bootique-flyway.svg)](https://travis-ci.org/bootique/bootique-flyway)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.bootique.flyway/bootique-flyway/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.bootique.flyway/bootique-flyway/)

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
            <version>0.21</version>
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
compile("io.bootique.flyway:bootique-flyway:0.1")
```

*Note:* **bootique-flyway** is a part of [bootique-bom](https://github.com/bootique/bootique-bom), and version can be 
imported from there.


## Available commands

Flyway is based around just 6 commands: Migrate, Clean, Info, Validate, Baseline and Repair. They are presented in 
Bootique-Flyway module:

```
OPTIONS
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

## Example Project

[bootique-flyway-demo](https://github.com/bootique-examples/bootique-flyway-demo)
