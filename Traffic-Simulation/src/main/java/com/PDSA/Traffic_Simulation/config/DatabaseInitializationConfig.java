package com.PDSA.Traffic_Simulation.config;

import org.springframework.context.annotation.Configuration;

/**
 * Database configuration - Hibernate manages schema creation/updates
 * based on entity annotations with @Column(columnDefinition = "LONGTEXT")
 */
@Configuration
public class DatabaseInitializationConfig {
    // No additional initialization needed - Hibernate handles table creation/updates
}
