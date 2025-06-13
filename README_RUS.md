# Document Platform

![CI Status](https://github.com/tennyros/document-platform/workflows/CI%20Pipeline%20with%20Maven/badge.svg)
![Java 11](https://img.shields.io/badge/Java-11-blue)
![Spring Boot 2.7](https://img.shields.io/badge/Spring_Boot-2.7-brightgreen)

[Русский](README_RUS.md) | [English](README.md)

**Document Platform** — микросервисное веб-приложение для управления электронными документами и отчетами.  
Данный репозиторий содержит реализацию микросервиса **Document Management Service** (DMS), который отвечает за:

- загрузку, хранение и удаление документов;
- версионирование документов;
- хранение и поиск по метаданным;
- интеграцию с S3-совместимыми хранилищами (MinIO, Amazon S3).

## Стек технологий

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

## Быстрый старт

### Требования

- Java 11+
- Maven 3.6+
- Docker + Docker Compose

### Настройка

**1. Клонируйте репозиторий:**

```bash
git clone https://github.com/tennyros/document-platform.git
cd document-platform
```

**2. Скопируйте файл .env:**

```bash
cp .env.example .env
```

**3. Запустите приложение и сопутствующие сервисы через Docker:**

```bash
# Скопируйте docker-compose.yml файл
cp docker-compose.example.yml docker-compose.yml  

# Запуск
docker-compose up -d
```

**4. После этого API будет доступен по адресу:**

```url
http://localhost:8090/swagger-ui.html
```

## API-эндпоинты

### Document-management-service

```http
POST   /documents        - Загрузить документ
GET    /documents        - Получить список документов с пагинацией и фильтрацией
DELETE /documents/{name} - Удалить документ по его UUID

POST   /documents/{documentId}/versions - Загрузить версию документа
DELETE /documents/{versionId}           - Удалить версию документа
GET    /documents/{documentId}/versions/{versionNumber}/download - Скачать документ определенной версии
```

## CI Pipeline

Проект настроен с CI для автоматической сборки с использованием GitHub Actions.
