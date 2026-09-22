# BalancedMind API

Spring Boot API for user authentication and an AI-powered therapeutic chat experience. The API acts as a secure gateway to the external TheraAPI service.

> **Disclaimer:** BalancedMind is an educational and portfolio project. It does not provide medical diagnosis, emergency support or professional mental-health care.

## Contents

- [Architecture](#architecture)
- [Technologies](#technologies)
- [Running the project](#running-the-project)
- [Environment configuration](#environment-configuration)
- [Authentication and security](#authentication-and-security)
- [API documentation](#api-documentation)
- [Endpoints](#endpoints)
- [Project structure](#project-structure)

## Architecture

BalancedMind is organized as a layered Spring Boot application:

- **Application layer:** DTOs, services and mappings
- **Domain layer:** domain models and repositories
- **Infrastructure layer:** REST controllers, external API clients, security and exception handling
- **Persistence:** database configuration is defined in `application.yaml`
- **External integration:** TheraAPI, accessed through the dedicated `TheraAPIClient`

The API exposes authentication endpoints and a protected AI chat endpoint. The backend validates the user identity before forwarding chat requests to the external AI service.

## Technologies

- Java 17+
- Spring Boot
- Spring Web
- Spring Security
- Firebase Authentication and JWT validation
- Jakarta Bean Validation
- Maven
- OpenAPI/Swagger

## Running the project

### Requirements

- JDK 17 or later
- Maven 3.8 or later
- Firebase project and Admin SDK credentials
- TheraAPI access credentials

### Run locally

From the project root:

```bash
mvn spring-boot:run
```

The application starts by default at:

```text
http://localhost:8080
```

The API base path is:

```text
http://localhost:8080/api/v1
```

When deployed, the API is available at:

```text
https://balancedmind.lat/api/v1
```

Port and profile settings are defined in `src/main/resources/application.yaml`.

## Environment configuration

Configure the following integrations for each environment:

### Firebase

BalancedMind uses Firebase for user authentication and JWT-based identity validation. Firebase Admin SDK credentials must be supplied through a secure environment-specific mechanism, such as environment variables, a mounted secret or a secret manager.

Do not commit Firebase service-account JSON files, private keys or other credentials to the repository.

Relevant application components include:

- `FirebaseConfig.java`
- `SecurityConfig.java`

### TheraAPI

Configure the TheraAPI endpoint and credentials through the application configuration and the external client configuration used by `TheraAPIClient`.

Use separate settings for development, staging and production. Never publish API keys or tokens in source code, README files or screenshots.

## Authentication and security

Authentication is based on Firebase and JWTs.

Typical authentication flow:

1. `POST /api/v1/auth/signup` creates a user in the local application database.
2. `POST /api/v1/auth/login` authenticates the user and returns an `idToken` and `refreshToken`.
3. `POST /api/v1/auth/refresh` issues a new access token from a valid refresh token.

Protected endpoints, including `/api/v1/ai/chat`, require the following header:

```http
Authorization: Bearer <idToken>
```

## API documentation

The OpenAPI specification is available in:

```text
swagger-balancedmind.yaml
```

You can import the specification into:

- [Swagger UI](https://swagger.io/tools/swagger-ui/)
- [Postman](https://www.postman.com/)
- [Insomnia](https://insomnia.rest/)

To use Swagger UI locally, load `swagger-balancedmind.yaml` into the interface.

## Endpoints

### Authentication

#### `POST /api/v1/auth/signup`

Creates a new user.

Request body:

```json
{
  "email": "user@example.com",
  "password": "secure-password",
  "username": "username"
}
```

Validation rules:

- `email`: required and must be a valid email address
- `password`: required, between 6 and 24 characters
- `username`: required, up to 120 characters

Returns a `UserProfileResponse` with public user information.

#### `POST /api/v1/auth/login`

Authenticates a user and returns authentication tokens.

Request body:

```json
{
  "email": "user@example.com",
  "password": "secure-password"
}
```

Returns a `LoginResponse` containing:

- `user`
- `idToken`
- `refreshToken`
- `expiresIn`

#### `POST /api/v1/auth/refresh`

Refreshes the access token using a valid refresh token.

Request body:

```json
{
  "refreshToken": "refresh-token"
}
```

Returns a `TokenRefreshResponse` containing:

- `idToken`
- `refreshToken`
- `expiresIn`

### AI chat

#### `POST /api/v1/ai/chat`

Sends a message to the therapeutic AI chat through TheraAPI.

Required header:

```http
Authorization: Bearer <idToken>
```

Request body:

```json
{
  "message": "I have been feeling stressed at work.",
  "moodRating": 3
}
```

Fields:

- `message`: required user message
- `moodRating`: optional integer associated with the chat session

Responses:

- `200 OK`: `AiChatResponse`
  - `response`: AI-generated response
  - `suggestedTechniques`: optional list of suggested techniques
- `400 Bad Request`: invalid request data
- `500 Internal Server Error`: error while processing the request

## Project structure

```text
src/main/java/
└── ...
    ├── application/   # DTOs, services and mappings
    ├── domain/         # Domain models and repositories
    └── infra/          # REST, external clients, security and exceptions
```

## Project status

BalancedMind is maintained as a portfolio project demonstrating Spring Boot API design, JWT-based authentication, external AI service integration and layered backend architecture.
