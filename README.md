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

## Report Answers

### Part 1: Service Architecture & Setup

#### Question 1
The default lifecycle of JAX-RS (e.g. Jersey) is **per-request**, meaning a new resource instance is created for each HTTP request. Sub-resources follow the same behaviour unless explicitly configured (e.g., `@Singleton`).

As a result, instance variables cannot store shared state. In this project, shared data is stored in static structures within `DataStore`. In a real-world system, thread-safe structures such as `ConcurrentHashMap` or proper synchronisation would be required to prevent race conditions.

#### Question 2
HATEOAS allows APIs to include navigational links in responses, making them self-discoverable.

**Key benefits:**
- Reduces client-server coupling (no hardcoded URLs)
- Supports API evolution without breaking clients
- Provides contextual navigation

A basic implementation of this principle is demonstrated by the Discovery endpoint.

### Part 2: Room Management

#### Question 1
Returning only IDs reduces payload size but introduces additional requests (N+1 problem). Returning full objects increases payload size but improves usability.

This API returns full `Room` objects due to the small dataset and to simplify client interaction. For larger datasets, pagination would be recommended.

#### Question 2
DELETE is idempotent in this API. The first request removes the resource (204 No Content), while subsequent requests return 404 Not Found. The system state remains unchanged after the initial deletion.

### Part 3: Sensor Operations & Linking

#### Question 1
The `@Consumes(MediaType.APPLICATION_JSON)` annotation ensures that the endpoint only accepts JSON input. Requests with other formats are automatically rejected with **415 Unsupported Media Type**, preventing invalid processing.

#### Question 2
Query parameters (e.g., `/sensors?type=CO2`) are preferred for filtering because they are optional, flexible and support multiple criteria.

Path parameters are better suited for identifying specific resources (e.g., `/sensors/{id}`).

### Part 4: Deep Nesting with Sub-Resources

#### Question
The Sub-Resource Locator pattern improves API design by enabling clear separation of concerns. In this implementation, `SensorResource` delegates reading-related operations to a dedicated `SensorReadingResource`, ensuring each class has a focused responsibility.

This structure enhances readability and maintainability by avoiding large, monolithic resource classes. Instead of handling all nested paths (e.g., `/sensors/{id}/readings/{rid}`) in one place, logic is distributed across smaller, specialised components.

As a result, the codebase becomes easier to test, extend and manage. This approach is particularly useful for large-scale APIs with deep resource hierarchies, where modularity and reusability are essential.

### Part 5: Advanced Error Handling, Exception Mapping & Logging

#### Question 1
HTTP 422 is more appropriate when the request is valid but contains invalid data (e.g., a non-existent `roomId`). In contrast, 404 indicates that the requested resource does not exist.

#### Question 2
Exposing stack traces can reveal internal details such as class structure, framework versions and file paths. This creates security risks (CWE-209). The API prevents this by using a generic exception mapper.

#### Question 3
`ContainerRequestFilter` and `ContainerResponseFilter` enable centralised logging, maintain separation of concerns and avoid code duplication. This approach is more maintainable than adding logging logic in every resource method.

## Postman Tests

**Collection Name:** *Smart Campus API - Full Demonstration*

### 1. Part 1: Setup & Discovery

#### 1.1 Discovery Endpoint

**GET** `http://localhost:8080/api/v1`

**Expected:**

* Clean JSON response
* Includes: name, version, contact and resources map

### 2. Part 2: Room Management

#### 2.1 Create Room 1

**POST** `http://localhost:8080/api/v1/rooms`

**Body (JSON):**

```json
{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 50
}
```

**Expected:**

* `201 Created`
* Room object returned

#### 2.2 Create Room 2

**POST** `http://localhost:8080/api/v1/rooms`

**Body (JSON):**

```json
{
  "id": "CS-101",
  "name": "Computer Lab",
  "capacity": 30
}
```

**Expected:**

* `201 Created`

#### 2.3 List All Rooms

**GET** `http://localhost:8080/api/v1/rooms`

**Expected:**

* Array containing both rooms

#### 2.4 Get Single Room

**GET** `http://localhost:8080/api/v1/rooms/LIB-301`

**Expected:**

* Single room object

#### 2.5 Delete Room without Sensors (Normal Case)

**DELETE** `http://localhost:8080/api/v1/rooms/LIB-301`

**Expected:**

* `204 No Content`

### 3. Part 3: Sensors & Filtering

#### 3.1 Create Sensor (Valid Room)

**POST** `http://localhost:8080/api/v1/sensors`

**Body (JSON):**

```json
{
  "id": "TEMP-001",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 22.5,
  "roomId": "LIB-301"
}
```

**Expected:**

* `201 Created`

#### 3.2 Create Sensor (Invalid Room)

**POST** `http://localhost:8080/api/v1/sensors`

**Body (JSON):**

```json
{
  "id": "BAD-001",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 20,
  "roomId": "NON-EXISTENT"
}
```

**Expected:**

* `422 Unprocessable Entity`
* JSON error response

#### 3.3 List All Sensors

**GET** `http://localhost:8080/api/v1/sensors`

#### 3.4 Filter Sensors by Type

**GET** `http://localhost:8080/api/v1/sensors?type=Temperature`

**Expected:**

* Only Temperature sensors returned

#### 3.5 Delete Sensor

**DELETE** `http://localhost:8080/api/v1/sensors/TEMP-001`

#### 3.6 Delete Room (Now Successful)

**DELETE** `http://localhost:8080/api/v1/rooms/LIB-301`

**Expected:**

* `204 No Content`

### 4. Part 4: Sub-Resources (Readings)

#### 4.1 Create Sensor for Readings

**POST** `http://localhost:8080/api/v1/sensors`

**Body (JSON):**

```json
{
  "id": "TEMP-002",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 23.0,
  "roomId": "CS-101"
}
```

#### 4.2 Add First Reading

**POST** `http://localhost:8080/api/v1/sensors/TEMP-002/readings`

**Body (JSON):**

```json
{
  "value": 24.5
}
```

**Expected:**

* `201 Created`
* Reading object returned

#### 4.3 Add Second Reading

**POST** `http://localhost:8080/api/v1/sensors/TEMP-002/readings`

**Body (JSON):**

```json
{
  "value": 25.1
}
```

---

#### 4.4 Get Sensor Readings History

**GET** `http://localhost:8080/api/v1/sensors/TEMP-002/readings`

**Expected:**

* Array with 2 readings

### 5. Part 5: Error Handling & Logging

#### 5.1 Create MAINTENANCE Sensor

**POST** `http://localhost:8080/api/v1/sensors`

**Body (JSON):**

```json
{
  "id": "OCC-999",
  "type": "Occupancy",
  "status": "MAINTENANCE",
  "currentValue": 0,
  "roomId": "CS-101"
}
```

#### 5.2 Delete Room with Sensors (Conflict Case)

**DELETE** `http://localhost:8080/api/v1/rooms/CS-101`

**Expected:**

* `409 Conflict`
* JSON error (`ROOM_NOT_EMPTY`)

#### 5.3 Add Reading to MAINTENANCE Sensor (Forbidden)

**POST** `http://localhost:8080/api/v1/sensors/OCC-999/readings`

**Body (JSON):**

```json
{
  "value": 10
}
```

**Expected:**

* `403 Forbidden`
* JSON error response

#### 5.4 Trigger 404 Error

**GET** `http://localhost:8080/api/v1/rooms/INVALID-ID`

**Expected:**

* `404 Not Found`
* Demonstrates exception mapper

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
**W2038271**
Module: *5COSC022W - Client-Server Architectures*  
University of Westminster
