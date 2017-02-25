[![Build Status](https://travis-ci.org/bootique/bootique-flyway.svg)](https://travis-ci.org/bootique/bootique-flyway)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.bootique.flyway/bootique-flyway/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.bootique.flyway/bootique-flyway/)

# bootique-flyway

Provides [Flyway](https://flywaydb.org/) migrations framework integration with [Bootique](http://bootique.io).

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

## Create first migration

**src/main/resources/db/migration/V1__Create_person_table.sql:**
```sql
create table PERSON (
    ID int not null,
    NAME varchar(100) not null
);
```

## Define datasources

**config.yml:**
```yaml
jdbc:
  h2:
    url: jdbc:h2:~/bootique-flyway-test
    username: 'username'
    password: 'password'
    driverClassName: org.h2.Driver

flyway:
  locations:
    - db/migration # default, can be omitted
  dataSources:
    - h2
```

## Run commands

```bash
java -jar app.jar --config=config.yml --migrate
```

## Available commands

```
-b, --baseline
   Baselines an existing database, excluding all migrations up to and
   including baselineVersion.

--clean
   Drops all objects (tables, views, procedures, triggers, ...) in the
   configured schemas.The schemas are cleaned in the order specified by
   the schemas property.

-i, --info
   Prints the details and status information about all the migrations.

-m, --migrate
   Migrates the schema to the latest version. Flyway will create the
   metadata table automatically if it doesn't exist.

-r, --repair
   Repairs the metadata table.

-v, --validate
   Validate applied migrations against resolved ones (on the filesystem
   or classpath) to detect accidental changes that may prevent the
   schema(s) from being recreated exactly.
```

## Example Project

[bootique-flyway-example](https://github.com/bootique/bootique-flyway/tree/master/bootique-flyway-example)
