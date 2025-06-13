# Document Platform

![CI Status](https://github.com/tennyros/document-platform/workflows/CI%20Pipeline%20with%20Maven/badge.svg)
![Java 11](https://img.shields.io/badge/Java-11-blue)
![Spring Boot 2.7](https://img.shields.io/badge/Spring_Boot-2.7-brightgreen)

[Русский](README_RUS.md) | [English](README.md)

**Document Platform** is a microservice-based web application for managing electronic documents and reports.  
This repository contains the implementation of the **Document Management Service** (DMS), responsible for:

- Uploading, storing, and deleting documents
- Document versioning
- Storing and searching by metadata
- Integration with S3-compatible storage systems (MinIO, Amazon S3)

## Technology Stack

- Java 11
- Spring Boot 2.7
- Spring Web
- Hibernate / Spring Data JPA
- PostgreSQL / Liquibase
- MongoDB / Mongock
- MinIO
- Elasticsearch + Logstash + Kibana (logging)
- Docker, Docker Compose
- MapStruct
- Lombok

## Quick Start

### Requirements

- Java 11+
- Maven 3.6+
- Docker + Docker Compose

### Setup

**1. Clone the repository:**

```bash
git clone https://github.com/tennyros/document-platform.git
cd document-platform
```

**2. Copy the .env file:**

```bash
cp .env.example .env
```

**3. Launch the application and related services using Docker:**

```bash
# Copy the example Docker Compose file
cp docker-compose.example.yml docker-compose.yml  

# Start services
docker-compose up -d
```

**4. After that, the API will be available at:**

```url
http://localhost:8090/swagger-ui.html
```

## API Endpoints

### Document-management-service

```http
POST   /documents        - Upload a document
GET    /documents        - Retrieve a list of documents with pagination and filtering
DELETE /documents/{name} - Delete a document by its UUID

POST   /documents/{documentId}/versions - Upload a document version
DELETE /documents/{versionId}           - Delete certain document version
GET    /documents/{documentId}/versions/{versionNumber}/download - Download certain document version
```

## CI Pipeline

The project is configured with CI using GitHub Actions for automatic builds.
