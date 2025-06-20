# Document Platform

![CI Status](https://github.com/tennyros/document-platform/workflows/CI%20Pipeline%20with%20Maven/badge.svg)
![Java 11](https://img.shields.io/badge/Java-11-blue)
![Spring Boot 2.7](https://img.shields.io/badge/Spring_Boot-2.7-brightgreen)

[Документация на русском языке](README_RUS.md)

**Document Platform** is a microservice-based web application for managing electronic documents and reports.  
This repository contains the implementation of:

**Document Management Service** (DMS), responsible for:

- Uploading, storing, and deleting documents
- Document versioning
- Storing and searching by metadata
- Integration with S3-compatible storage systems (MinIO, Amazon S3)

**Document Signing Service** (DSS) is responsible for generating and verifying digital signatures for documents.

Implemented features:
- Retrieves document hash from DMS via HTTP
- Generates digital signature using RSA and SHA256
- Verifies digital signature against a stored public key
- Stores signature metadata in DMS (signer, timestamp, certificate info)
- Configurable RSA key loading via external path (private.key, public.key)
- Uses Bouncy Castle as cryptography provider

## Technology Stack

- Java 11
- Spring Boot 2.7
- Spring Web
- Spring Data REST
- Hibernate / Spring Data JPA
- PostgreSQL / Liquibase
- MongoDB / Mongock
- WebClient
- Bouncy Castle (SHA256withRSA)
- MinIO
- Elasticsearch + Logstash + Kibana (logging)
- Docker, Docker Compose
- MapStruct
- Lombok
- SpringDoc OpenAPI 3 (Swagger UI)

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

**3. Generate for signing service RSA key pair for local development:**

```bash
chmod +x scripts/generate-keys.sh
./scripts/generate-keys.sh
```

**4. Launch the application and related services using Docker:**

```bash
# Copy the example Docker Compose file
cp docker-compose.example.yml docker-compose.yml  

# Make sure the ports specified in docker-compose are not in use on your system and start
docker-compose up -d
```

**5. After that, the API will be available at:**

```url
http://localhost:8090/swagger-ui.html (Document Management Service)
http://localhost:8091/swagger-ui.html (Document Signing Service)
```

## API Endpoints

### Document Management Service

#### Documents

| Method | Endpoint               | Description                                                |
|--------|------------------------|------------------------------------------------------------|
| POST   | `/documents`           | Upload a new document (metadata + file)                   |
| GET    | `/documents`           | Retrieve a list of documents with pagination and filters  |
| DELETE | `/documents/{documentId}` | Delete a document along with all its versions             |

---

#### Document Versions

| Method | Endpoint                                                | Description                                                |
|--------|---------------------------------------------------------|------------------------------------------------------------|
| POST   | `/documents/{documentId}/versions`                      | Upload a new version for the specified document            |
| DELETE | `/documents/{documentId}/versions/{versionId}`          | Delete a specific version of a document                    |
| GET    | `/documents/{documentId}/versions/{versionId}/download` | Download a specific version of a document                  |

---

#### Signatures and Hashes (used by Document Signing Service)

| Method | Endpoint                                                   | Description                                                |
|--------|------------------------------------------------------------|------------------------------------------------------------|
| GET    | `/documents/{documentId}/versions/{versionId}/hash`       | Retrieve the Base64-encoded hash of the document version   |
| POST   | `/documents/{documentId}/versions/{versionId}/signatures` | Save a digital signature for the given document version    |
|        |                                                            | _Invoked from Document Signing Service (DSS) via WebClient_ |

### Document Signing Service

| Метод | Endpoint             | Описание          |
|-------|----------------------|-------------------|
| POST  | `/signatures/sign`   | Sign document     |
| GET   | `/signatures/verify` | Sign verification |

## CI Pipeline

The project is configured with CI using GitHub Actions for automatic builds.
