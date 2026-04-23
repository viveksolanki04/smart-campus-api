# Smart Campus API

A RESTful Sensor & Room Management API built with **JAX-RS (Jersey)** and Grizzly embedded server for the University of Westminster module **5COSC022W – Client-Server Architectures (2025/26)**.

## Overview

This API manages Rooms and Sensors on a university campus. It supports:
- CRUD operations on rooms and sensors
- Linking sensors to rooms with integrity checks
- Historical sensor readings with sub-resource nesting
- Advanced error handling (409, 422, 403, 500) using custom exception mappers
- Request/response logging for observability

The API strictly follows REST principles and uses only in-memory data structures (`HashMap`) as required.

**Base URL:** `http://localhost:8080/api/v1`

## Technologies Used
- Java 17
- Maven
- JAX-RS 2.1 (Jersey 2.35)
- Grizzly HTTP Server (embedded)
- Jackson for JSON processing

**No databases, no Spring Boot, no external frameworks** — just JAX-RS as per the requirements.

## How to Build and Run

1. Clone the repository:
   ```bash
   git clone https://github.com/viveksolanki04/smart-campus-api.git
   cd smart-campus-api

2. Build and start the server:
