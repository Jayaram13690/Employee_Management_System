package com.emp.management.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class to load environment variables from .env file.
 * Uses EnvironmentPostProcessor to load .env early in the startup process,
 * before Spring tries to resolve properties.
 */
public class EnvConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // Load .env file from the backend directory
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMissing()
                .load();

        // Convert .env entries to a map and add to Spring environment
        Map<String, Object> envProperties = new HashMap<>();
        dotenv.entries().forEach(entry -> {
            envProperties.put(entry.getKey(), entry.getValue());
        });

        // Add the loaded properties to Spring's property sources
        // This makes them available for ${VARIABLE_NAME} placeholder resolution
        MapPropertySource propertySource = new MapPropertySource("dotenv", envProperties);
        environment.getPropertySources().addFirst(propertySource);
    }
}
