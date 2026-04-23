package com.smartcampus.exception;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class SensorNotFoundExceptionMapper implements ExceptionMapper<SensorNotFoundException> {

    @Override
    public Response toResponse(SensorNotFoundException ex) {

        ErrorResponse error = new ErrorResponse(
                "SENSOR_NOT_FOUND",
                ex.getMessage(),
                404
        );

        return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
    }
}