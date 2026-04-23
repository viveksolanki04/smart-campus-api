package com.smartcampus.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
    @Override
    public Response toResponse(SensorUnavailableException ex) {
        ErrorResponse error = new ErrorResponse("SENSOR_UNAVAILABLE", ex.getMessage(), 403);
        return Response.status(Response.Status.FORBIDDEN).entity(error).build();
    }
}