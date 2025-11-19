# BalancedMind API

API de autenticação de usuários e chat terapêutico com IA (gateway para TheraAPI), construída em Spring Boot.

## Índice
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Execução do projeto](#execução-do-projeto)
- [Configuração de ambiente](#configuração-de-ambiente)
- [Autenticação e segurança](#autenticação-e-segurança)
- [Documentação da API (Swagger/OpenAPI)](#documentação-da-api-swaggeropenapi)
- [Endpoints](#endpoints)
  - [/auth](#auth)
  - [/ai](#ai)
- [Estrutura de pastas](#estrutura-de-pastas)

---

## Arquitetura

- **Backend:** Spring Boot (Java)
- **Camadas principais:**
  - `application`: DTOs, services e mapeamentos
  - `domain`: modelos de domínio e repositórios
  - `infra`: controllers REST, clients externos, segurança e exceções
- **Banco de dados:** (configuração definida em `application.yaml`)
- **Integração externa:** TheraAPI (chat terapêutico com IA) via `TheraAPIClient`.

## Tecnologias

- Java 17+ (recomendado)
- Spring Boot
- Spring Web
- Spring Security (JWT / Firebase)
- Jakarta Validation (Bean Validation)
- Maven

## Execução do projeto

### Requisitos
- JDK 17+ instalado
- Maven 3.8+ instalado

### Rodando localmente

Na raiz do projeto:

```bash
mvn spring-boot:run
```

O backend subirá por padrão em:

- `http://localhost:8080`

O servidor está aberto para chamadas em:
- `https://balancedmind.lat/api/v1/**`

As configurações de porta e perfis estão em `src/main/resources/application.yaml`.

## Configuração de ambiente

Alguns pontos importantes de configuração:

- **Firebase**
  - Arquivo de credenciais: `src/main/resources/gs2fiap-firebase-adminsdk-fbsvc-594aeebb70.json`
  - Configurações adicionais em `FirebaseConfig.java` e `SecurityConfig.java`.
- **TheraAPI**
  - Endpoint e credenciais configurados em `WebClientConfig.java` e/ou `application.yaml`.

Adapte esses arquivos para os ambientes de **desenvolvimento**, **homologação** e **produção** antes de subir a aplicação.

## Autenticação e segurança

- Autenticação baseada em **Firebase** com tokens **JWT**.
- O fluxo típico é:
  1. `POST /auth/signup` – cria o usuário na base local.
  2. `POST /auth/login` – autentica o usuário e retorna `idToken` e `refreshToken`.
  3. `POST /auth/refresh` – obtém um novo `idToken` a partir do `refreshToken`.
- Endpoints protegidos (como `/ai/chat`) exigem header:

  `Authorization: Bearer <idToken>`

## Documentação da API (Swagger/OpenAPI)

A especificação OpenAPI está no arquivo:

- `swagger-balancedmind.yaml`

Você pode importá-lo em ferramentas como:

- [Swagger UI](https://swagger.io/tools/swagger-ui/)
- [Postman](https://www.postman.com/)
- [Insomnia](https://insomnia.rest/)

Para usar com Swagger UI localmente, basta apontar a interface para o arquivo `swagger-balancedmind.yaml`.

## Endpoints

### /auth

**Base URL:** `/auth`

#### POST /auth/signup
Cria um novo usuário.

- **Request body (JSON):**
  - `email` (string, email, obrigatório)
  - `password` (string, 6–24, obrigatório)
  - `username` (string, até 120, obrigatório)

- **Response 200:**
  - `UserProfileResponse` com dados públicos do usuário.

#### POST /auth/login
Autentica o usuário e retorna tokens JWT.

- **Request body (JSON):**
  - `email` (string, email, obrigatório)
  - `password` (string, 6–24, obrigatório)

- **Response 200:**
  - `LoginResponse` contendo:
    - `user` (perfil do usuário)
    - `idToken`
    - `refreshToken`
    - `expiresIn`

#### POST /auth/refresh
Atualiza o token de acesso a partir de um `refreshToken` válido.

- **Request body (JSON):**
  - `refreshToken` (string, obrigatório)

- **Response 200:**
  - `TokenRefreshResponse` contendo:
    - `idToken`
    - `refreshToken`
    - `expiresIn`

### /ai

**Base URL:** `/ai`

#### POST /ai/chat
Envia uma mensagem para o chat de IA (TheraAPI) e retorna uma resposta terapêutica.

- **Headers:**
  - `Authorization: Bearer <idToken>` (obrigatório)

- **Request body (JSON):** `AiChatRequest`
  - `message` (string, obrigatório) – mensagem do usuário para a IA.
  - `moodRating` (integer, opcional) – identificador da sessão de chat.

- **Responses:**
  - `200` – `AiChatResponse`
    - `response` (string) – resposta da IA.
    - `suggestedTechniques` (array de string, opcional) – técnicas sugeridas.
  - `400` – erro de validação (dados inválidos).
  - `500` – erro interno ao processar a requisição.

---

