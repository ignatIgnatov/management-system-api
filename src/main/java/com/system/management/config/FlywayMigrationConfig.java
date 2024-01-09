package com.system.management.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component

public class FlywayMigrationConfig {
    @Autowired
    FlywayMigrationConfig(DataSource dataSource) {
        Flyway.configure()
                .baselineOnMigrate(true)
                .validateMigrationNaming(true)
                .dataSource(dataSource)
                .load()
                .migrate();
    }
}
