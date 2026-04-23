package com.smartcampus.resource;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.exception.SensorNotFoundException;
import com.smartcampus.model.Sensor;
import com.smartcampus.service.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.stream.Collectors;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public List<Sensor> getSensors(@QueryParam("type") String type) {

        List<Sensor> all = new ArrayList<>(DataStore.sensors.values());

        if (type != null) {
            return all.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }

        return all;
    }

    @POST
    public Response createSensor(Sensor sensor) {

        if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room does not exist: " + sensor.getRoomId());
        }

        DataStore.sensors.put(sensor.getId(), sensor);

        DataStore.rooms.get(sensor.getRoomId())
                .getSensorIds()
                .add(sensor.getId());

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getSensor(@PathParam("id") String id) {

        Sensor sensor = DataStore.sensors.get(id);

        if (sensor == null) {
            throw new SensorNotFoundException("Sensor not found: " + id);
        }

        return Response.ok(sensor).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSensor(@PathParam("id") String id) {

        Sensor sensor = DataStore.sensors.get(id);

        if (sensor == null) {
            throw new SensorNotFoundException("Sensor not found: " + id);
        }

        String roomId = sensor.getRoomId();
        if (DataStore.rooms.containsKey(roomId)) {
            DataStore.rooms.get(roomId).getSensorIds().remove(id);
        }

        DataStore.sensors.remove(id);

        return Response.noContent().build();
    }

    // SUB-RESOURCE LOCATOR
    @Path("/{id}/readings")
    public SensorReadingResource getReadings() {
        return new SensorReadingResource();
    }
}