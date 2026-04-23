package com.smartcampus.exception;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {

        ErrorResponse error = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "Unexpected server error occurred",
                500
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .build();
    }
}