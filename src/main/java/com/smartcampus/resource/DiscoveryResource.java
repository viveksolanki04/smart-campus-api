package com.smartcampus.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Response getApiInfo() {

        Map<String, Object> response = new HashMap<>();

        response.put("name", "Smart Campus API");
        response.put("version", "v1");
        response.put("contact", "admin@smartcampus.com");

        Map<String, String> resources = new HashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");

        response.put("resources", resources);

        return Response.ok(response).build();
    }
}