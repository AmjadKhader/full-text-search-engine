# Full-Text Search Service

This is a simple full-text search service written in Java using Spring Boot. It allows you to store and search text
documents based on their content.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [API Endpoints](#api-endpoints)

## Features

* Store and retrieve text documents
* Search documents based on keywords
* Bulk upload documents

## Installation

1. Clone this repository.
2. Ensure you have Java 17, Maven installed, Docker is installed and running.
3. Configure the application properties in src/main/resources/application.yaml if needed (e.g., database
   settings).
4. Build the project using Maven: ```mvn clean install```
5. Build docker image: ```docker build -t text-search-engine .```
6. Run docker compose: ```docker compose up -d```
7. The application will start on http://localhost:8092.
   You can now use any API testing tool (e.g., Postman) to interact with the endpoints.

## API Endpoints

This service provides a REST API for interacting with documents and performing searches.

### Add Document

```
curl -X POST http://localhost:8092/text-search-engine/api/documents -H 'Content-Type: application/json' -d '{"content": "This is a sample document content."}'
```

### Add Multiple Documents

```
curl -X POST http://localhost:8092/text-search-engine/api/documents -H 'Content-Type: application/json' -d '{"contents": ["This is a sample document content."]}'
```

### Get Document By Id

``` 
curl --location 'http://localhost:8092/text-search-engine/api/documents/d842852e-3c4a-4d40-bc4e-5f1e1aad03e3'
```

### Get All Documents

``` 
curl --location 'http://localhost:8092/text-search-engine/api/documents/'
```

### Delete Document
```
curl --location --request DELETE 'http://localhost:8092/text-search-engine/api/documents/60e053f2-62f4-4df7-8503-ff248e690936'
```

### Search

Search for documents containing "sample" and "document":

```
curl http://localhost:8092/text-search-engine/api/search?q=sample,document
```

