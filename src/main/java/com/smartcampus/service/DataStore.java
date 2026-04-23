package com.smartcampus.service;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;

import java.util.HashMap;
import java.util.Map;

public class DataStore {

    public static Map<String, Room> rooms = new HashMap<>();
    public static Map<String, Sensor> sensors = new HashMap<>();
}