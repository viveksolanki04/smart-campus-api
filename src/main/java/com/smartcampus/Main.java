package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

import com.smartcampus.config.ApplicationConfig;

public class Main {

    public static void main(String[] args) {

        URI baseUri = URI.create("http://localhost:8080/api/v1/");

        ResourceConfig config = new ApplicationConfig();

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);

        System.out.println("Smart Campus API started at http://localhost:8080/api/v1");

        try {
            server.start();
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}