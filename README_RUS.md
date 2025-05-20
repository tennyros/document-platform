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
- PostgreSQL
- MinIO
- Docker, Docker Compose
- Elasticsearch + Logback + Kibana (логирование)

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
http://localhost:8088/swagger-ui.html
```

## API-эндпоинты

### Document-management-service

```http
POST   /documents/upload          - Загрузить документ
GET    /documents                 - Получить список документов
GET    /documents/download/{name} - Получить документ по его UUID
DELETE /documents/delete/{name}   - Удалить документ по его UUID
```

## CI Pipeline

Проект настроен с CI для автоматической сборки с использованием GitHub Actions.
