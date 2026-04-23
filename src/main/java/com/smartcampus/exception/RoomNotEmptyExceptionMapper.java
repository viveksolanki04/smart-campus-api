package com.smartcampus.exception;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {

        ErrorResponse error = new ErrorResponse(
                "ROOM_NOT_EMPTY",
                ex.getMessage(),
                409
        );

        return Response.status(Response.Status.CONFLICT)
                .entity(error)
                .build();
    }
}