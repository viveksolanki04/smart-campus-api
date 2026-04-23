package com.smartcampus.resource;

import com.smartcampus.exception.SensorNotFoundException;
import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.service.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    public SensorReadingResource() {}   

    @GET
    public Response getReadings(@PathParam("id") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new SensorNotFoundException("Sensor not found: " + sensorId);
        }
        return Response.ok(sensor.getReadings()).build();
    }

    @POST
    public Response addReading(@PathParam("id") String sensorId, SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new SensorNotFoundException("Sensor not found: " + sensorId);
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is in MAINTENANCE mode and cannot accept readings");
        }

        reading.setId(UUID.randomUUID().toString());
        reading.setTimestamp(System.currentTimeMillis());

        sensor.getReadings().add(reading);
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}