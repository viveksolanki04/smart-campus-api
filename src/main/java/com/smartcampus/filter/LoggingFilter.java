package com.smartcampus.filter;

import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger logger = Logger.getLogger("SmartCampusLogger");

    @Override
    public void filter(ContainerRequestContext requestContext) {

        logger.info("REQUEST: " +
                requestContext.getMethod() +
                " " +
                requestContext.getUriInfo().getRequestUri());
    }

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) {

        logger.info("RESPONSE STATUS: " + responseContext.getStatus());
    }
}