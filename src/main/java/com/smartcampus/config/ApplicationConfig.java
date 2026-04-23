package com.smartcampus.config;

import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        packages(
                "com.smartcampus.resource",
                "com.smartcampus.exception",
                "com.smartcampus.filter"
        );
    }
}