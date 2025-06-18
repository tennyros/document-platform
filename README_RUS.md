# Document Platform

![CI Status](https://github.com/tennyros/document-platform/workflows/CI%20Pipeline%20with%20Maven/badge.svg)
![Java 11](https://img.shields.io/badge/Java-11-blue)
![Spring Boot 2.7](https://img.shields.io/badge/Spring_Boot-2.7-brightgreen)

[English documentation](README.md)

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
- Spring Data REST
- Hibernate / Spring Data JPA
- PostgreSQL / Liquibase
- MongoDB / Mongock
- MinIO
- Elasticsearch + Logstash + Kibana (logging)
- Docker, Docker Compose
- MapStruct
- Lombok
- SpringDoc OpenAPI 3 (Swagger UI)

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

# Убедитесь, что порты на Вашей системе, указанные в docker-compose, не заняты и запустите
docker-compose up -d
```

**4. После этого API будет доступен по адресу:**

```url
http://localhost:8090/swagger-ui.html
```

## API-эндпоинты

### Document Management Service

#### Документы

| Метод | Endpoint            | Описание                                             |
|-------|---------------------|------------------------------------------------------|
| POST  | `/documents`        | Загрузить новый документ (метаданные + файл)         |
| GET   | `/documents`        | Получить список документов с пагинацией и фильтрацией|
| DELETE| `/documents/{documentId}` | Удалить документ и все его версии                    |

---

#### Версии документов

| Метод | Endpoint                                        | Описание                           |
|-------|-------------------------------------------------|------------------------------------|
| POST  | `/documents/{documentId}/versions`              | Загрузить новую версию документа   |
| DELETE| `/documents/{documentId}/versions/{versionId}`  | Удалить конкретную версию документа |
| GET   | `/documents/{documentId}/versions/{versionId}/download`| Скачать указанную версию документа |

---

#### Подписи и хэши (используется Document Signing Service)

| Метод | Endpoint                                                  | Описание                                                                 |
|-------|-----------------------------------------------------------|--------------------------------------------------------------------------|
| GET   | `/documents/{documentId}/versions/{versionId}/hash`       | Получить Base64-хэш содержимого версии документа                         |
| POST  | `/documents/{documentId}/versions/{versionId}/signatures` | Сохранить электронную подпись к версии документа                         |
|       |                                                           | _Вызывается из DSS через WebClient_                                      |

## CI Pipeline

Проект настроен с CI для автоматической сборки с использованием GitHub Actions.
