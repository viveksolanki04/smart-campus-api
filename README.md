# Smart Campus API

A RESTful Sensor & Room Management API built with **JAX-RS (Jersey)** and an embedded Grizzly server for the University of Westminster module
**5COSC022W – Client-Server Architectures (2025/26)**.

## Overview

This API manages **Rooms** and **Sensors** on a university campus. It supports:

* CRUD operations on rooms and sensors
* Linking sensors to rooms with integrity checks
* Historical sensor readings with sub-resource nesting
* Advanced error handling (409, 422, 403, 500) using custom exception mappers
* Request/response logging for observability

The API strictly follows REST principles and uses only in-memory data structures (`HashMap`) as required.

**Base URL:**

```
http://localhost:8080/api/v1
```

## Technologies Used

* Java 17
* Maven
* JAX-RS 2.1 (Jersey 2.35)
* Grizzly HTTP Server (embedded)
* Jackson (JSON processing)

> No databases, no Spring Boot, no external frameworks - JAX-RS as per the requirements.

## How to Build and Run

### 1. Clone the repository

```bash
git clone https://github.com/viveksolanki04/smart-campus-api.git
cd smart-campus-api
```

### 2. Build and start the server

```bash
mvn clean compile exec:java
```

### 3. Server Output

```
Smart Campus API started at http://localhost:8080/api/v1
```

## API Endpoints

### 1. Discovery

```
GET /api/v1
```

Returns API metadata and available resource links.

### 2. Rooms

```
GET    /api/v1/rooms        → List all rooms
POST   /api/v1/rooms        → Create a new room
GET    /api/v1/rooms/{id}   → Get room by ID
DELETE /api/v1/rooms/{id}   → Delete room (blocked if sensors assigned)
```

### 3. Sensors

```
GET    /api/v1/sensors             → List all sensors (?type=Temperature supported)
POST   /api/v1/sensors             → Create a sensor (validates room exists)
GET    /api/v1/sensors/{id}        → Get sensor
DELETE /api/v1/sensors/{id}        → Delete sensor
```

### 4. Sensor Readings (Sub-resource)

```
GET  /api/v1/sensors/{id}/readings → Get reading history
POST /api/v1/sensors/{id}/readings → Add new reading (updates currentValue)
```

## Sample cURL Commands

### 1. Discovery

```bash
curl -X GET http://localhost:8080/api/v1
```

### 2. Create a Room

```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"LIB-301","name":"Library Quiet Study","capacity":50}'
```

### 3. List Rooms

```bash
curl -X GET http://localhost:8080/api/v1/rooms
```

### 4. Create a Sensor

```bash
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":22.5,"roomId":"LIB-301"}'
```

### 5. Add a Sensor Reading

```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":23.8}'
```

## Project Structure

```
smart-campus-api/
├── pom.xml
├── src/main/java/com/smartcampus/
│   ├── Main.java
│   ├── config/
│   │   └── ApplicationConfig.java
│   ├── exception/        (custom exceptions + mappers)
│   ├── filter/
│   │   └── LoggingFilter.java
│   ├── model/            (Room, Sensor, SensorReading)
│   ├── resource/         (DiscoveryResource, RoomResource, SensorResource, SensorReadingResource)
│   └── service/
│       └── DataStore.java
└── target/               (generated after build)
```

---

## Report / Coursework Submission

Detailed answers to all assignment questions are provided in the **separate Report PDF** submitted via Blackboard.

> This README focuses only on setup and API usage as required.

## Video Demonstration

A **10-minute video demonstration** has been recorded and submitted via Blackboard.

The video includes:

* Full API walkthrough using Postman
* Successful CRUD operations
* Error handling scenarios (409, 422, 403, 500)

## Repository

GitHub:
[https://github.com/viveksolanki04/smart-campus-api](https://github.com/viveksolanki04/smart-campus-api)

## Author

**Vivek Solanki**  
Module: *5COSC022W - Client-Server Architectures*  
University of Westminster
