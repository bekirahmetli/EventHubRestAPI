# EventHubRestAPI

![EventHub Logo](uploads/eventhub.jpeg)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue.svg)](https://www.postgresql.org/)

A comprehensive Spring Boot REST API for Event Management with JWT authentication, OAuth2 integration (GitHub, Google), refresh token mechanism, and full CRUD operations for events, categories, users, ticket types, and registrations.

## 📋 Table of Contents / İçindekiler

- [English](#english)
  - [Overview](#overview)
  - [Features](#features)
  - [Architecture](#architecture)
  - [Technologies](#technologies)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Configuration](#configuration)
  - [Database Setup](#database-setup)
  - [Usage](#usage)
  - [Authentication](#authentication)
  - [API Endpoints](#api-endpoints)
  - [Project Structure](#project-structure)
  - [Testing](#testing)
  - [Screenshots](#screenshots)
  - [Contributing](#contributing)
  - [License](#license)

- [Türkçe](#türkçe)
  - [Genel Bakış](#genel-bakış)
  - [Özellikler](#özellikler)
  - [Mimari](#mimari)
  - [Teknolojiler](#teknolojiler)
  - [Gereksinimler](#gereksinimler)
  - [Kurulum](#kurulum)
  - [Yapılandırma](#yapılandırma)
  - [Veritabanı Kurulumu](#veritabanı-kurulumu)
  - [Kullanım](#kullanım)
  - [Kimlik Doğrulama](#kimlik-doğrulama)
  - [API Endpoint'leri](#api-endpointleri)
  - [Proje Yapısı](#proje-yapısı)
  - [Test Etme](#test-etme)
  - [Ekran Görüntüleri](#ekran-görüntüleri)
  - [Katkıda Bulunma](#katkıda-bulunma)
  - [Lisans](#lisans)

---

## English

### Overview

EventHubRestAPI is a production-ready Spring Boot REST API application designed for event management and ticket sales. The API provides comprehensive functionality for managing events, categories, users, ticket types, and registrations with a robust authentication system using JWT tokens, refresh tokens, and OAuth2 integration.

**Key Highlights:**
- ✅ Full CRUD operations for all entities
- ✅ JWT-based authentication with refresh token support
- ✅ OAuth2 integration (GitHub, Google)
- ✅ Image upload support for event posters
- ✅ Layered architecture (Controller → Service → Repository)
- ✅ DTO-based design with validation
- ✅ Pagination support
- ✅ Comprehensive error handling
- ✅ RESTful API best practices
- ✅ Swagger API documentation

### Features

- **Authentication & Authorization**
  - JWT token-based authentication (20-minute expiry)
  - Refresh token mechanism (4-hour expiry)
  - Role-based access control (USER, ORGANIZER, ADMIN)
  - Secure password encryption with BCrypt
  - OAuth2 integration (GitHub, Google)
  - Stateless session management

- **Event Management**
  - Complete CRUD operations
  - Event poster image upload
  - Category-based event filtering
  - Organizer-based event filtering
  - Event-TicketType relationships
  - Pagination support

- **Ticket Management**
  - Ticket types with pricing and quotas
  - Registration management
  - Registration status tracking (ACTIVE, CANCELED)
  - User registration history

- **User Management**
  - Multiple authentication providers (LOCAL, GOOGLE, GITHUB)
  - Role management (USER, ORGANIZER, ADMIN)
  - Avatar URL support
  - User event creation and registration tracking

- **API Features**
  - RESTful API design
  - Standardized response format (Result/ResultData)
  - Comprehensive validation
  - Error handling with meaningful messages
  - CORS configuration
  - Swagger UI documentation

### Architecture

The project follows a **layered architecture** pattern:

```
┌─────────────────────────────────────┐
│         API Layer (Controllers)     │
│  - REST endpoints                   │
│  - Request/Response handling        │
│  - Authentication filter            │
│  - Image upload handling            │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Business Layer (Services)      │
│  - Business logic                   │
│  - Transaction management           │
│  - Exception handling               │
│  - Image storage service            │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│    Data Access Layer (Repositories) │
│  - Spring Data JPA                  │
│  - Database operations              │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Entity Layer (Models)        │
│  - JPA entities                     │
│  - Relationships                    │
└─────────────────────────────────────┘
```

**Package Structure:**
- `api/` - REST Controllers
- `business/abstracts/` - Service interfaces
- `business/concretes/` - Service implementations
- `dao/` - Spring Data JPA repositories
- `entities/` - JPA entities
- `dto/request/` - Request DTOs with validation
- `dto/response/` - Response DTOs
- `config/` - Configuration classes (Security, Swagger, ModelMapper)
- `exception/` - Custom exceptions and global exception handler
- `jwt/` - JWT and security components
- `mapper/` - ModelMapper service
- `result/` - Standardized response wrappers
- `enums/` - Enumeration types

### Technologies

- **Framework**: Spring Boot 4.0.0
- **Language**: Java 21
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT (jjwt 0.12.6) + OAuth2 Client
- **Object Mapping**: ModelMapper 3.1.1
- **Validation**: Jakarta Validation
- **Utilities**: Lombok
- **ORM**: Spring Data JPA / Hibernate
- **API Documentation**: SpringDoc OpenAPI 2.8.14
- **File Upload**: MultipartFile (Spring Boot)

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+** (or use the included Maven Wrapper)
- **PostgreSQL 15+** (or compatible version)
- **Git** (for cloning the repository)

### Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd EventHubRestAPI
   ```

2. **Create PostgreSQL database**:
   ```sql
   CREATE DATABASE event_hub_rest_api;
   ```

3. **Configure database connection** in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/event_hub_rest_api
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   ```

4. **Build the project**:
   ```bash
   ./mvnw clean install
   ```
   Or on Windows:
   ```bash
   mvnw.cmd clean install
   ```

5. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```
   Or on Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

The application will start on `http://localhost:8080` by default.

### Configuration

#### Application Properties

The main configuration file is located at `src/main/resources/application.properties`:

```properties
spring.application.name=EventHubRestAPI

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/event_hub_rest_api
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.show-sql=true

# OAuth2 Configuration
spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID:your_client_id}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET:your_client_secret}

# Logging
logging.level.org.springframework.security=${SPRING_SECURITY_LOG_LEVEL:TRACE}
logging.pattern.console=${LOGPATTERN_CONSOLE:%green(%d{HH:mm:ss.SSS}) %blue(%-5level) %red([%thread]) %yellow(%logger{15}) - %msg%n}
```

**Key Configuration Options:**
- `spring.jpa.hibernate.ddl-auto=update` - Automatically creates/updates database schema
- `spring.jpa.hibernate.show-sql=true` - Shows SQL queries in console (set to `false` in production)
- OAuth2 credentials can be set via environment variables or directly in properties

#### JWT Configuration

JWT settings are configured in `JwtService.java`:
- **Secret Key**: Defined as constant
- **Access Token Expiry**: 20 minutes (1200000 ms)
- **Refresh Token Expiry**: 4 hours (14400000 ms)

#### OAuth2 Configuration

To configure OAuth2:

1. **GitHub OAuth App**:
   - Go to GitHub → Settings → Developer settings → OAuth Apps
   - Create a new OAuth App
   - Set Authorization callback URL: `http://localhost:8080/oauth2/callback/github`
   - Copy Client ID and Client Secret
   - Update `application.properties` with your credentials

   **GitHub OAuth Login Screen:**
   
   ![OAuth2 GitHub Login](uploads/oauth.png)

2. **Google OAuth** (optional):
   - Go to Google Cloud Console
   - Create OAuth 2.0 credentials
   - Add redirect URI: `http://localhost:8080/oauth2/callback/google`
   - Update `application.properties` with your credentials

### Database Setup

#### Automatic Schema Creation

With `spring.jpa.hibernate.ddl-auto=update`, Hibernate will automatically create/update the database schema on application startup.

#### Manual Schema Setup (Optional)

If you prefer manual setup, the schema will be created automatically by Hibernate on first run. Entity relationships are defined in the entity classes with JPA annotations.

#### Seed Data

Sample data is provided in `db/seed.sql`. To load it:

```bash
psql -U postgres -d event_hub_rest_api -f db/seed.sql
```

Or using pgAdmin or any PostgreSQL client, execute the SQL file.

**Seed Data Includes:**
- 8 categories (Müzik, Spor, Teknoloji, Sanat, Eğitim, Kültür, Eğlence, İş & Kariyer)
- 5 users (1 Admin, 2 Organizers, 2 Regular Users)
  - Default password for all users: `password123`
- 8 events with various categories and organizers
- 16 ticket types across different events
- 8 registrations with different statuses

**Database Diagram:**

![Database Diagram](docs/dbdiagram.png)

**Note on User Passwords:**
- All seed users have the password: `password123`
- The password is hashed using BCrypt in the seed file
- In production, always use the API registration endpoint to ensure proper password hashing

### Usage

1. **Start the application** (see [Installation](#installation))

2. **Access the API**:
   - Base URL: `http://localhost:8080`
   - API endpoints are prefixed with `/v1/`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

3. **Authenticate**:
   - Register a new user via `/v1/auth/register`
   - Or login using seed data credentials
   - Use OAuth2: `http://localhost:8080/oauth2/authorization/github`

4. **Use Postman Collection**:
   - Import `postman/EventhubRestAPI.postman_collection.json` into Postman
   - All endpoints are pre-configured with sample requests

### Authentication

#### Register

**Endpoint**: `POST /v1/auth/register`

**Request Body**:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "authProvider": "LOCAL",
  "role": "USER"
}
```

**Response** (201 Created):
```json
{
  "success": true,
  "message": "201",
  "httpStatus": "201",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "uuid-string-here",
    "tokenType": "Bearer",
    "userId": 1,
    "email": "john@example.com",
    "role": "USER"
  }
}
```

#### Login

**Endpoint**: `POST /v1/auth/authenticate`

**Request Body**:
```json
{
  "email": "john@example.com",
  "password": "password123",
  "authProvider": "LOCAL"
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "200",
  "httpStatus": "200",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "new-uuid-string",
    "tokenType": "Bearer",
    "userId": 1,
    "email": "john@example.com",
    "role": "USER"
  }
}
```

#### Refresh Token

**Endpoint**: `POST /v1/auth/refresh`

**Request Body**:
```json
{
  "refreshToken": "uuid-string-from-login-response"
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "200",
  "httpStatus": "200",
  "data": {
    "token": "new-access-token",
    "refreshToken": "new-refresh-token",
    "tokenType": "Bearer",
    "userId": 1,
    "email": "john@example.com",
    "role": "USER"
  }
}
```

#### Logout

**Endpoint**: `POST /v1/auth/logout`

**Request Body**:
```json
{
  "refreshToken": "uuid-string-to-invalidate"
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "200",
  "httpStatus": "200"
}
```

#### OAuth2 Login

Navigate to:
- GitHub: `http://localhost:8080/oauth2/authorization/github`
- Google: `http://localhost:8080/oauth2/authorization/google` (if configured)

After successful authentication, you'll be redirected to the callback URL with token information.

#### Using Access Token

Include the access token in the `Authorization` header for all protected endpoints:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Protected Endpoints**: All endpoints except `/v1/auth/**` and `/oauth2/**` require authentication.

### API Endpoints

#### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/v1/auth/register` | Register new user | No |
| POST | `/v1/auth/authenticate` | Login and get tokens | No |
| POST | `/v1/auth/refresh` | Refresh access token | No |
| POST | `/v1/auth/logout` | Logout and invalidate refresh token | No |
| GET | `/oauth2/authorization/{provider}` | OAuth2 login (github/google) | No |

#### Category Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/v1/categories` | Create category | Yes |
| GET | `/v1/categories/{id}` | Get category by ID | Yes |
| GET | `/v1/categories` | Get all categories (paginated) | Yes |
| PUT | `/v1/categories` | Update category | Yes |
| DELETE | `/v1/categories/{id}` | Delete category | Yes |

**Request Example (Create)**:
```json
{
  "name": "Müzik"
}
```

**Query Parameters (List)**:
- `page` (default: 0) - Page number
- `pageSize` (default: 10) - Items per page

#### User Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/v1/users` | Create user | Yes |
| GET | `/v1/users/{id}` | Get user by ID | Yes |
| GET | `/v1/users` | Get all users (paginated) | Yes |
| PUT | `/v1/users` | Update user | Yes |
| DELETE | `/v1/users/{id}` | Delete user | Yes |

**Request Example (Create)**:
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "password123",
  "authProvider": "LOCAL",
  "role": "ORGANIZER"
}
```

#### Event Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/v1/events` | Create event (JSON) | Yes |
| POST | `/v1/events/with-poster` | Create event with poster (Multipart) | Yes |
| GET | `/v1/events/{id}` | Get event by ID | Yes |
| GET | `/v1/events` | Get all events (paginated) | Yes |
| GET | `/v1/events/category/{categoryId}` | Get events by category | Yes |
| GET | `/v1/events/organizer/{organizerId}` | Get events by organizer | Yes |
| PUT | `/v1/events` | Update event | Yes |
| DELETE | `/v1/events/{id}` | Delete event | Yes |

**Request Example (Create)**:
```json
{
  "title": "Rock Festival 2024",
  "description": "Büyük rock festivali! 20+ sanatçı sahne alacak.",
  "posterUrl": "https://example.com/posters/rock-festival.jpg",
  "location": "Ankara Hipodrom",
  "date": "2024-07-15T18:00:00",
  "categoryId": 1,
  "userId": 2
}
```

**Request Example (Create with Poster)**:
```
POST /v1/events/with-poster
Content-Type: multipart/form-data

title: Rock Festival 2024
description: Büyük rock festivali!
location: Ankara Hipodrom
date: 2024-07-15T18:00:00
categoryId: 1
userId: 2
poster: [binary file]
```

#### Ticket Type Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/v1/ticket-types` | Create ticket type | Yes |
| GET | `/v1/ticket-types/{id}` | Get ticket type by ID | Yes |
| GET | `/v1/ticket-types` | Get all ticket types (paginated) | Yes |
| PUT | `/v1/ticket-types` | Update ticket type | Yes |
| DELETE | `/v1/ticket-types/{id}` | Delete ticket type | Yes |

**Request Example (Create)**:
```json
{
  "name": "VIP",
  "price": 500.00,
  "quota": 100,
  "eventId": 1
}
```

#### Registration Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/v1/registrations` | Create registration | Yes |
| GET | `/v1/registrations/{id}` | Get registration by ID | Yes |
| GET | `/v1/registrations` | Get all registrations (paginated) | Yes |
| GET | `/v1/registrations/user/{userId}` | Get registrations by user | Yes |
| GET | `/v1/registrations/ticket-type/{ticketTypeId}` | Get registrations by ticket type | Yes |
| GET | `/v1/registrations/status/{status}` | Get registrations by status | Yes |
| PUT | `/v1/registrations` | Update registration | Yes |
| DELETE | `/v1/registrations/{id}` | Delete registration | Yes |

**Request Example (Create)**:
```json
{
  "userId": 3,
  "ticketTypeId": 1,
  "status": "ACTIVE"
}
```

### Project Structure

```
EventHubRestAPI/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           ├── EventHubRestApiApplication.java
│   │   │           ├── api/                          # REST Controllers
│   │   │           │   ├── AuthenticationController.java
│   │   │           │   ├── CategoryController.java
│   │   │           │   ├── EventController.java
│   │   │           │   ├── RegistrationController.java
│   │   │           │   ├── TicketTypeController.java
│   │   │           │   └── UserController.java
│   │   │           ├── business/                     # Business Logic
│   │   │           │   ├── abstracts/               # Service Interfaces
│   │   │           │   │   ├── IAuthenticationService.java
│   │   │           │   │   ├── ICategoryService.java
│   │   │           │   │   ├── IEventService.java
│   │   │           │   │   ├── IImageStorageService.java
│   │   │           │   │   ├── IRegistrationService.java
│   │   │           │   │   ├── ITicketTypeService.java
│   │   │           │   │   └── IUserService.java
│   │   │           │   └── concretes/               # Service Implementations
│   │   │           │       ├── AuthManager.java
│   │   │           │       ├── CategoryManager.java
│   │   │           │       ├── EventManager.java
│   │   │           │       ├── ImageStorageManager.java
│   │   │           │       ├── RegistrationManager.java
│   │   │           │       ├── TicketTypeManager.java
│   │   │           │       └── UserManager.java
│   │   │           ├── config/                      # Configuration
│   │   │           │   ├── AppConfig.java          # Security beans
│   │   │           │   ├── ModelMapperConfig.java  # ModelMapper config
│   │   │           │   ├── SecurityConfig.java     # Security filter chain
│   │   │           │   └── SwaggerConfig.java      # Swagger/OpenAPI config
│   │   │           ├── dao/                        # Repositories
│   │   │           │   ├── CategoryRepo.java
│   │   │           │   ├── EventRepo.java
│   │   │           │   ├── RegistrationRepo.java
│   │   │           │   ├── TicketTypeRepo.java
│   │   │           │   └── UserRepo.java
│   │   │           ├── dto/                        # Data Transfer Objects
│   │   │           │   ├── request/
│   │   │           │   │   ├── auth/
│   │   │           │   │   ├── category/
│   │   │           │   │   ├── event/
│   │   │           │   │   ├── registration/
│   │   │           │   │   ├── ticketType/
│   │   │           │   │   └── user/
│   │   │           │   └── response/
│   │   │           ├── entities/                   # JPA Entities
│   │   │           │   ├── Category.java
│   │   │           │   ├── Event.java
│   │   │           │   ├── Registration.java
│   │   │           │   ├── TicketType.java
│   │   │           │   └── User.java
│   │   │           ├── enums/                      # Enumerations
│   │   │           │   ├── AuthProvider.java
│   │   │           │   ├── RegistrationStatus.java
│   │   │           │   └── Role.java
│   │   │           ├── exception/                  # Exception Handling
│   │   │           │   ├── AlreadyExistsException.java
│   │   │           │   ├── EventNotFoundException.java
│   │   │           │   ├── GlobalExceptionHandler.java
│   │   │           │   ├── ImageUploadException.java
│   │   │           │   └── NotFoundException.java
│   │   │           ├── jwt/                        # JWT Components
│   │   │           │   ├── AuthEntryPoint.java
│   │   │           │   ├── JwtAuthenticationFilter.java
│   │   │           │   ├── JwtService.java
│   │   │           │   ├── OAuth2FailureHandler.java
│   │   │           │   └── OAuth2SuccessHandler.java
│   │   │           ├── mapper/                     # ModelMapper
│   │   │           │   ├── IModelMapperService.java
│   │   │           │   └── ModelManagerService.java
│   │   │           └── result/                     # Response Wrappers
│   │   │               ├── Result.java
│   │   │               ├── ResultData.java
│   │   │               └── ResultHelper.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/                            # Static files
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── EventHubRestApiApplicationTests.java
├── db/
│   └── seed.sql                                   # Sample database data
├── docs/
│   ├── dbdiagram.png                             # Database diagram
│   └── swagger-ss.png                            # Swagger documentation screenshot
├── postman/
│   └── EventhubRestAPI.postman_collection.json   # Postman collection
├── uploads/
│   └── eventhub.jpeg                             # Project logo
├── pom.xml                                        # Maven dependencies
├── mvnw                                           # Maven wrapper (Unix)
├── mvnw.cmd                                       # Maven wrapper (Windows)
└── README.md                                      # This file
```

### Testing

#### Using Postman

1. **Import Collection**:
   - Open Postman
   - Click "Import"
   - Select `postman/EventhubRestAPI.postman_collection.json`

2. **Test Authentication**:
   - Run "register" request to create a user
   - Run "login" request to get access token
   - Copy the `token` from response
   - Set it in collection variables or use it in Authorization header

3. **Test Endpoints**:
   - All endpoints are organized by entity
   - Each endpoint has sample request bodies
   - Make sure to update IDs in requests based on your data

#### Manual Testing

**Example: Create an Event**

1. **Login**:
   ```bash
   curl -X POST http://localhost:8080/v1/auth/authenticate \
     -H "Content-Type: application/json" \
     -d '{
       "email": "admin@eventhub.com",
       "password": "password123",
       "authProvider": "LOCAL"
     }'
   ```

2. **Create Category** (if not exists):
   ```bash
   curl -X POST http://localhost:8080/v1/categories \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
     -d '{
       "name": "Müzik"
     }'
   ```

3. **Create Event**:
   ```bash
   curl -X POST http://localhost:8080/v1/events \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
     -d '{
       "title": "Rock Festival 2024",
       "description": "Büyük rock festivali!",
       "location": "Ankara Hipodrom",
       "date": "2024-07-15T18:00:00",
       "categoryId": 1,
       "userId": 2
     }'
   ```

### Screenshots

#### Swagger Documentation

![Swagger Documentation](docs/swagger-ss.png)

Swagger UI is available at `http://localhost:8080/swagger-ui.html` once the application is running.

#### Database Schema

![Database Diagram](docs/dbdiagram.png)

The diagram shows all entity relationships in the database.

### Error Handling

The API uses a standardized error response format:

```json
{
  "success": false,
  "message": "Error message here",
  "httpStatus": "400"
}
```

**Common Error Codes:**
- `400` - Bad Request (validation errors)
- `401` - Unauthorized (authentication required)
- `404` - Not Found (resource not found)
- `500` - Internal Server Error

**Validation Errors**:
```json
{
  "success": false,
  "message": "Validation failed",
  "httpStatus": "400",
  "data": [
    "Event title cannot be empty",
    "Event date must be in the future"
  ]
}
```

### Business Rules

1. **Event Creation**:
   - Event must have a valid `categoryId` and `userId` (organizer)
   - Event date must be in the future
   - Poster URL is optional but recommended

2. **Ticket Type Creation**:
   - Ticket type must belong to an existing event
   - Price must be positive
   - Quota must be positive

3. **Registration**:
   - User can register for any ticket type
   - Registration status can be ACTIVE or CANCELED
   - Registration timestamp is automatically set

4. **Authentication**:
   - Access tokens expire after 20 minutes
   - Refresh tokens expire after 4 hours
   - One refresh token per user (old token is deleted on new login)

5. **OAuth2**:
   - Users can login with GitHub or Google
   - OAuth2 users are automatically created if they don't exist
   - Email is used as unique identifier

---

## Türkçe

### Genel Bakış

EventHubRestAPI, etkinlik yönetimi ve bilet satışı için tasarlanmış production-ready bir Spring Boot REST API uygulamasıdır. API, JWT token'lar, refresh token'lar ve OAuth2 entegrasyonu ile güçlü bir kimlik doğrulama sistemiyle etkinlikler, kategoriler, kullanıcılar, bilet tipleri ve kayıtlar için kapsamlı işlevsellik sağlar.

**Önemli Özellikler:**
- ✅ Tüm entity'ler için tam CRUD işlemleri
- ✅ Refresh token desteği ile JWT tabanlı kimlik doğrulama
- ✅ OAuth2 entegrasyonu (GitHub, Google)
- ✅ Etkinlik posterleri için görsel yükleme desteği
- ✅ Katmanlı mimari (Controller → Service → Repository)
- ✅ Validasyon ile DTO tabanlı tasarım
- ✅ Sayfalama desteği
- ✅ Kapsamlı hata yönetimi
- ✅ RESTful API en iyi uygulamaları
- ✅ Swagger API dokümantasyonu

### Özellikler

- **Kimlik Doğrulama ve Yetkilendirme**
  - JWT token tabanlı kimlik doğrulama (20 dakika süre)
  - Refresh token mekanizması (4 saatlik süre)
  - Rol tabanlı erişim kontrolü (USER, ORGANIZER, ADMIN)
  - BCrypt ile güvenli şifre şifreleme
  - OAuth2 entegrasyonu (GitHub, Google)
  - Stateless session yönetimi

- **Etkinlik Yönetimi**
  - Tam CRUD işlemleri
  - Etkinlik poster görseli yükleme
  - Kategori bazlı etkinlik filtreleme
  - Organizatör bazlı etkinlik filtreleme
  - Etkinlik-Bilet Tipi ilişkileri
  - Sayfalama desteği

- **Bilet Yönetimi**
  - Fiyatlandırma ve kota ile bilet tipleri
  - Kayıt yönetimi
  - Kayıt durumu takibi (ACTIVE, CANCELED)
  - Kullanıcı kayıt geçmişi

- **Kullanıcı Yönetimi**
  - Birden fazla kimlik doğrulama sağlayıcısı (LOCAL, GOOGLE, GITHUB)
  - Rol yönetimi (USER, ORGANIZER, ADMIN)
  - Avatar URL desteği
  - Kullanıcı etkinlik oluşturma ve kayıt takibi

- **API Özellikleri**
  - RESTful API tasarımı
  - Standartlaştırılmış yanıt formatı (Result/ResultData)
  - Kapsamlı validasyon
  - Anlamlı mesajlarla hata yönetimi
  - CORS yapılandırması
  - Swagger UI dokümantasyonu

### Mimari

Proje **katmanlı mimari** desenini takip eder:

```
┌─────────────────────────────────────┐
│    API Katmanı (Controllers)        │
│  - REST endpoint'leri               │
│  - Request/Response işleme          │
│  - Kimlik doğrulama filtresi        │
│  - Görsel yükleme işleme            │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│   İş Mantığı Katmanı (Services)     │
│  - İş mantığı                       │
│  - İşlem yönetimi                   │
│  - Exception işleme                 │
│  - Görsel depolama servisi          │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│  Veri Erişim Katmanı (Repositories) │
│  - Spring Data JPA                  │
│  - Veritabanı işlemleri             │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Entity Katmanı (Models)        │
│  - JPA entity'leri                  │
│  - İlişkiler                        │
└─────────────────────────────────────┘
```

**Paket Yapısı:**
- `api/` - REST Controller'lar
- `business/abstracts/` - Servis arayüzleri
- `business/concretes/` - Servis implementasyonları
- `dao/` - Spring Data JPA repository'leri
- `entities/` - JPA entity'leri
- `dto/request/` - Validasyon ile Request DTO'ları
- `dto/response/` - Response DTO'ları
- `config/` - Yapılandırma sınıfları (Security, Swagger, ModelMapper)
- `exception/` - Özel exception'lar ve global exception handler
- `jwt/` - JWT ve güvenlik bileşenleri
- `mapper/` - ModelMapper servisi
- `result/` - Standartlaştırılmış yanıt wrapper'ları
- `enums/` - Enumeration tipleri

### Teknolojiler

- **Framework**: Spring Boot 4.0.0
- **Dil**: Java 21
- **Build Aracı**: Maven
- **Veritabanı**: PostgreSQL
- **Güvenlik**: Spring Security + JWT (jjwt 0.12.6) + OAuth2 Client
- **Nesne Eşleme**: ModelMapper 3.1.1
- **Validasyon**: Jakarta Validation
- **Yardımcılar**: Lombok
- **ORM**: Spring Data JPA / Hibernate
- **API Dokümantasyonu**: SpringDoc OpenAPI 2.8.14
- **Dosya Yükleme**: MultipartFile (Spring Boot)

### Gereksinimler

Başlamadan önce, aşağıdakilerin yüklü olduğundan emin olun:

- **Java 21** veya üzeri
- **Maven 3.6+** (veya dahil edilen Maven Wrapper'ı kullanın)
- **PostgreSQL 15+** (veya uyumlu sürüm)
- **Git** (depoyu klonlamak için)

### Kurulum

1. **Depoyu klonlayın**:
   ```bash
   git clone <repository-url>
   cd EventHubRestAPI
   ```

2. **PostgreSQL veritabanı oluşturun**:
   ```sql
   CREATE DATABASE event_hub_rest_api;
   ```

3. **Veritabanı bağlantısını yapılandırın** `src/main/resources/application.properties` dosyasında:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/event_hub_rest_api
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   ```

4. **Projeyi derleyin**:
   ```bash
   ./mvnw clean install
   ```
   Windows'ta:
   ```bash
   mvnw.cmd clean install
   ```

5. **Uygulamayı çalıştırın**:
   ```bash
   ./mvnw spring-boot:run
   ```
   Windows'ta:
   ```bash
   mvnw.cmd spring-boot:run
   ```

Uygulama varsayılan olarak `http://localhost:8080` adresinde başlayacaktır.

### Yapılandırma

#### Uygulama Özellikleri

Ana yapılandırma dosyası `src/main/resources/application.properties` konumundadır:

```properties
spring.application.name=EventHubRestAPI

# Veritabanı Yapılandırması
spring.datasource.url=jdbc:postgresql://localhost:5432/event_hub_rest_api
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.show-sql=true

# OAuth2 Yapılandırması
spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID:your_client_id}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET:your_client_secret}

# Logging
logging.level.org.springframework.security=${SPRING_SECURITY_LOG_LEVEL:TRACE}
logging.pattern.console=${LOGPATTERN_CONSOLE:%green(%d{HH:mm:ss.SSS}) %blue(%-5level) %red([%thread]) %yellow(%logger{15}) - %msg%n}
```

**Önemli Yapılandırma Seçenekleri:**
- `spring.jpa.hibernate.ddl-auto=update` - Veritabanı şemasını otomatik olarak oluşturur/günceller
- `spring.jpa.hibernate.show-sql=true` - Konsolda SQL sorgularını gösterir (production'da `false` yapın)
- OAuth2 kimlik bilgileri ortam değişkenleri veya doğrudan properties dosyasında ayarlanabilir

#### JWT Yapılandırması

JWT ayarları `JwtService.java` dosyasında yapılandırılmıştır:
- **Secret Key**: Sabit olarak tanımlanmış
- **Access Token Süresi**: 20 dakika (1200000 ms)
- **Refresh Token Süresi**: 4 saat (14400000 ms)

#### OAuth2 Yapılandırması

OAuth2'yi yapılandırmak için:

1. **GitHub OAuth App**:
   - GitHub → Settings → Developer settings → OAuth Apps'a gidin
   - Yeni bir OAuth App oluşturun
   - Authorization callback URL'yi ayarlayın: `http://localhost:8080/oauth2/callback/github`
   - Client ID ve Client Secret'ı kopyalayın
   - `application.properties` dosyasını kimlik bilgilerinizle güncelleyin

   **GitHub OAuth Giriş Ekranı:**
   
   ![OAuth2 GitHub Giriş](uploads/oauth.png)

2. **Google OAuth** (opsiyonel):
   - Google Cloud Console'a gidin
   - OAuth 2.0 kimlik bilgileri oluşturun
   - Redirect URI ekleyin: `http://localhost:8080/oauth2/callback/google`
   - `application.properties` dosyasını kimlik bilgilerinizle güncelleyin

### Veritabanı Kurulumu

#### Otomatik Şema Oluşturma

`spring.jpa.hibernate.ddl-auto=update` ile Hibernate, uygulama başlatıldığında veritabanı şemasını otomatik olarak oluşturur/günceller.

#### Manuel Şema Kurulumu (Opsiyonel)

Manuel kurulum tercih ederseniz, şema ilk çalıştırmada Hibernate tarafından otomatik olarak oluşturulacaktır. Entity ilişkileri JPA annotation'ları ile entity sınıflarında tanımlanmıştır.

#### Seed Veriler

Örnek veriler `db/seed.sql` dosyasında sağlanmıştır. Yüklemek için:

```bash
psql -U postgres -d event_hub_rest_api -f db/seed.sql
```

Veya pgAdmin veya herhangi bir PostgreSQL istemcisi kullanarak SQL dosyasını çalıştırın.

**Seed Veriler İçerir:**
- 8 kategori (Müzik, Spor, Teknoloji, Sanat, Eğitim, Kültür, Eğlence, İş & Kariyer)
- 5 kullanıcı (1 Admin, 2 Organizer, 2 Normal Kullanıcı)
  - Tüm kullanıcılar için varsayılan şifre: `password123`
- Farklı kategoriler ve organizatörlerle 8 etkinlik
- Farklı etkinliklerde 16 bilet tipi
- Farklı durumlarda 8 kayıt

**Veritabanı Şeması:**

![Veritabanı Diyagramı](docs/dbdiagram.png)

Diyagram, veritabanındaki tüm entity ilişkilerini gösterir.

**Kullanıcı Şifreleri Hakkında Not:**
- Tüm seed kullanıcıların şifresi: `password123`
- Şifre seed dosyasında BCrypt ile hash'lenmiştir
- Production'da her zaman düzgün şifre hash'lemesi için API kayıt endpoint'ini kullanın

### Kullanım

1. **Uygulamayı başlatın** (bkz. [Kurulum](#kurulum))

2. **API'ye erişin**:
   - Base URL: `http://localhost:8080`
   - API endpoint'leri `/v1/` ile başlar
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

3. **Kimlik doğrulama yapın**:
   - `/v1/auth/register` üzerinden yeni bir kullanıcı kaydedin
   - Veya seed veri kimlik bilgilerini kullanarak giriş yapın
   - OAuth2 kullanın: `http://localhost:8080/oauth2/authorization/github`

4. **Postman Koleksiyonunu Kullanın**:
   - `postman/EventhubRestAPI.postman_collection.json` dosyasını Postman'e import edin
   - Tüm endpoint'ler örnek isteklerle önceden yapılandırılmıştır

### Kimlik Doğrulama

#### Kayıt

**Endpoint**: `POST /v1/auth/register`

**İstek Gövdesi**:
```json
{
  "name": "Ahmet Yılmaz",
  "email": "ahmet@example.com",
  "password": "password123",
  "authProvider": "LOCAL",
  "role": "USER"
}
```

**Yanıt** (201 Created):
```json
{
  "success": true,
  "message": "201",
  "httpStatus": "201",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "uuid-string-here",
    "tokenType": "Bearer",
    "userId": 1,
    "email": "ahmet@example.com",
    "role": "USER"
  }
}
```

#### Giriş

**Endpoint**: `POST /v1/auth/authenticate`

**İstek Gövdesi**:
```json
{
  "email": "ahmet@example.com",
  "password": "password123",
  "authProvider": "LOCAL"
}
```

**Yanıt** (200 OK):
```json
{
  "success": true,
  "message": "200",
  "httpStatus": "200",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "new-uuid-string",
    "tokenType": "Bearer",
    "userId": 1,
    "email": "ahmet@example.com",
    "role": "USER"
  }
}
```

#### Token Yenileme

**Endpoint**: `POST /v1/auth/refresh`

**İstek Gövdesi**:
```json
{
  "refreshToken": "uuid-string-from-login-response"
}
```

**Yanıt** (200 OK):
```json
{
  "success": true,
  "message": "200",
  "httpStatus": "200",
  "data": {
    "token": "new-access-token",
    "refreshToken": "new-refresh-token",
    "tokenType": "Bearer",
    "userId": 1,
    "email": "ahmet@example.com",
    "role": "USER"
  }
}
```

#### Çıkış

**Endpoint**: `POST /v1/auth/logout`

**İstek Gövdesi**:
```json
{
  "refreshToken": "uuid-string-to-invalidate"
}
```

**Yanıt** (200 OK):
```json
{
  "success": true,
  "message": "200",
  "httpStatus": "200"
}
```

#### OAuth2 Giriş

Şu adrese gidin:
- GitHub: `http://localhost:8080/oauth2/authorization/github`
- Google: `http://localhost:8080/oauth2/authorization/google` (yapılandırılmışsa)

Başarılı kimlik doğrulamadan sonra, token bilgileriyle callback URL'sine yönlendirileceksiniz.

#### Access Token Kullanımı

Tüm korumalı endpoint'ler için `Authorization` header'ında access token'ı ekleyin:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Korumalı Endpoint'ler**: `/v1/auth/**` ve `/oauth2/**` dışındaki tüm endpoint'ler kimlik doğrulama gerektirir.

### API Endpoint'leri

#### Kimlik Doğrulama Endpoint'leri

| Method | Endpoint | Açıklama | Auth Gerekli |
|--------|----------|----------|--------------|
| POST | `/v1/auth/register` | Yeni kullanıcı kaydet | Hayır |
| POST | `/v1/auth/authenticate` | Giriş yap ve token al | Hayır |
| POST | `/v1/auth/refresh` | Access token yenile | Hayır |
| POST | `/v1/auth/logout` | Çıkış yap ve refresh token'ı geçersiz kıl | Hayır |
| GET | `/oauth2/authorization/{provider}` | OAuth2 giriş (github/google) | Hayır |

#### Kategori Endpoint'leri

| Method | Endpoint | Açıklama | Auth Gerekli |
|--------|----------|----------|--------------|
| POST | `/v1/categories` | Kategori oluştur | Evet |
| GET | `/v1/categories/{id}` | ID'ye göre kategori getir | Evet |
| GET | `/v1/categories` | Tüm kategorileri getir (sayfalı) | Evet |
| PUT | `/v1/categories` | Kategori güncelle | Evet |
| DELETE | `/v1/categories/{id}` | Kategori sil | Evet |

**İstek Örneği (Oluştur)**:
```json
{
  "name": "Müzik"
}
```

**Query Parametreleri (Liste)**:
- `page` (varsayılan: 0) - Sayfa numarası
- `pageSize` (varsayılan: 10) - Sayfa başına öğe sayısı

#### Kullanıcı Endpoint'leri

| Method | Endpoint | Açıklama | Auth Gerekli |
|--------|----------|----------|--------------|
| POST | `/v1/users` | Kullanıcı oluştur | Evet |
| GET | `/v1/users/{id}` | ID'ye göre kullanıcı getir | Evet |
| GET | `/v1/users` | Tüm kullanıcıları getir (sayfalı) | Evet |
| PUT | `/v1/users` | Kullanıcı güncelle | Evet |
| DELETE | `/v1/users/{id}` | Kullanıcı sil | Evet |

**İstek Örneği (Oluştur)**:
```json
{
  "name": "Ayşe Demir",
  "email": "ayse@example.com",
  "password": "password123",
  "authProvider": "LOCAL",
  "role": "ORGANIZER"
}
```

#### Etkinlik Endpoint'leri

| Method | Endpoint | Açıklama | Auth Gerekli |
|--------|----------|----------|--------------|
| POST | `/v1/events` | Etkinlik oluştur (JSON) | Evet |
| POST | `/v1/events/with-poster` | Poster ile etkinlik oluştur (Multipart) | Evet |
| GET | `/v1/events/{id}` | ID'ye göre etkinlik getir | Evet |
| GET | `/v1/events` | Tüm etkinlikleri getir (sayfalı) | Evet |
| GET | `/v1/events/category/{categoryId}` | Kategoriye göre etkinlikler | Evet |
| GET | `/v1/events/organizer/{organizerId}` | Organizatöre göre etkinlikler | Evet |
| PUT | `/v1/events` | Etkinlik güncelle | Evet |
| DELETE | `/v1/events/{id}` | Etkinlik sil | Evet |

**İstek Örneği (Oluştur)**:
```json
{
  "title": "Rock Festival 2024",
  "description": "Büyük rock festivali! 20+ sanatçı sahne alacak.",
  "posterUrl": "https://example.com/posters/rock-festival.jpg",
  "location": "Ankara Hipodrom",
  "date": "2024-07-15T18:00:00",
  "categoryId": 1,
  "userId": 2
}
```

**İstek Örneği (Poster ile Oluştur)**:
```
POST /v1/events/with-poster
Content-Type: multipart/form-data

title: Rock Festival 2024
description: Büyük rock festivali!
location: Ankara Hipodrom
date: 2024-07-15T18:00:00
categoryId: 1
userId: 2
poster: [binary file]
```

#### Bilet Tipi Endpoint'leri

| Method | Endpoint | Açıklama | Auth Gerekli |
|--------|----------|----------|--------------|
| POST | `/v1/ticket-types` | Bilet tipi oluştur | Evet |
| GET | `/v1/ticket-types/{id}` | ID'ye göre bilet tipi getir | Evet |
| GET | `/v1/ticket-types` | Tüm bilet tiplerini getir (sayfalı) | Evet |
| PUT | `/v1/ticket-types` | Bilet tipi güncelle | Evet |
| DELETE | `/v1/ticket-types/{id}` | Bilet tipi sil | Evet |

**İstek Örneği (Oluştur)**:
```json
{
  "name": "VIP",
  "price": 500.00,
  "quota": 100,
  "eventId": 1
}
```

#### Kayıt Endpoint'leri

| Method | Endpoint | Açıklama | Auth Gerekli |
|--------|----------|----------|--------------|
| POST | `/v1/registrations` | Kayıt oluştur | Evet |
| GET | `/v1/registrations/{id}` | ID'ye göre kayıt getir | Evet |
| GET | `/v1/registrations` | Tüm kayıtları getir (sayfalı) | Evet |
| GET | `/v1/registrations/user/{userId}` | Kullanıcıya göre kayıtlar | Evet |
| GET | `/v1/registrations/ticket-type/{ticketTypeId}` | Bilet tipine göre kayıtlar | Evet |
| GET | `/v1/registrations/status/{status}` | Duruma göre kayıtlar | Evet |
| PUT | `/v1/registrations` | Kayıt güncelle | Evet |
| DELETE | `/v1/registrations/{id}` | Kayıt sil | Evet |

**İstek Örneği (Oluştur)**:
```json
{
  "userId": 3,
  "ticketTypeId": 1,
  "status": "ACTIVE"
}
```

### Proje Yapısı

Proje yapısı yukarıdaki İngilizce bölümde detaylı olarak açıklanmıştır.

### Test Etme

#### Postman Kullanarak

1. **Koleksiyonu İçe Aktarın**:
   - Postman'i açın
   - "Import"a tıklayın
   - `postman/EventhubRestAPI.postman_collection.json` dosyasını seçin

2. **Kimlik Doğrulamayı Test Edin**:
   - "register" isteğini çalıştırarak kullanıcı oluşturun
   - "login" isteğini çalıştırarak access token alın
   - Yanıttan `token`'ı kopyalayın
   - Koleksiyon değişkenlerinde ayarlayın veya Authorization header'ında kullanın

3. **Endpoint'leri Test Edin**:
   - Tüm endpoint'ler entity'ye göre organize edilmiştir
   - Her endpoint'te örnek istek gövdeleri vardır
   - İsteklerdeki ID'leri verilerinize göre güncellediğinizden emin olun

### Ekran Görüntüleri

#### Swagger Dokümantasyonu

![Swagger Dokümantasyonu](docs/swagger-ss.png)

Swagger UI, uygulama çalıştığında `http://localhost:8080/swagger-ui.html` adresinde mevcuttur.

#### Veritabanı Şeması

![Veritabanı Diyagramı](docs/dbdiagram.png)

Diyagram, veritabanındaki tüm entity ilişkilerini gösterir.

### Hata Yönetimi

API standartlaştırılmış bir hata yanıt formatı kullanır:

```json
{
  "success": false,
  "message": "Hata mesajı burada",
  "httpStatus": "400"
}
```

**Yaygın Hata Kodları:**
- `400` - Bad Request (validasyon hataları)
- `401` - Unauthorized (kimlik doğrulama gerekli)
- `404` - Not Found (kaynak bulunamadı)
- `500` - Internal Server Error

**Validasyon Hataları**:
```json
{
  "success": false,
  "message": "Validation failed",
  "httpStatus": "400",
  "data": [
    "Etkinlik başlığı boş olamaz",
    "Etkinlik tarihi gelecekte olmalı"
  ]
}
```

### İş Kuralları

1. **Etkinlik Oluşturma**:
   - Etkinlik geçerli bir `categoryId` ve `userId` (organizatör) içermelidir
   - Etkinlik tarihi gelecekte olmalıdır
   - Poster URL'si opsiyoneldir ancak önerilir

2. **Bilet Tipi Oluşturma**:
   - Bilet tipi mevcut bir etkinliğe ait olmalıdır
   - Fiyat pozitif olmalıdır
   - Kota pozitif olmalıdır

3. **Kayıt**:
   - Kullanıcı herhangi bir bilet tipine kayıt olabilir
   - Kayıt durumu ACTIVE veya CANCELED olabilir
   - Kayıt zaman damgası otomatik olarak ayarlanır

4. **Kimlik Doğrulama**:
   - Access token'lar 20 dakika sonra sona erer
   - Refresh token'lar 4 saat sonra sona erer
   - Kullanıcı başına bir refresh token (yeni girişte eski token silinir)

5. **OAuth2**:
   - Kullanıcılar GitHub veya Google ile giriş yapabilir
   - OAuth2 kullanıcıları yoksa otomatik olarak oluşturulur
   - Email benzersiz tanımlayıcı olarak kullanılır

## 📞 Contact / İletişim

For questions or suggestions, please open an issue on GitHub.

Sorularınız veya önerileriniz için lütfen GitHub'da bir issue açın.

---

**Made with ❤️ using Spring Boot**

