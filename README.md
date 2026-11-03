# Library Management System

A Spring Boot REST API for managing library accounts, the book catalog, borrowing and returns, fines, and usage reports. The service uses JWT authentication and role-based access for administrator and librarian operations.

## Technology

- Java 17 and Spring Boot 3.3
- Spring Web, Spring Security, Spring Data JPA, and Bean Validation
- MySQL
- JWT (JJWT) and Spring Mail
- OpenAPI / Swagger UI

## Requirements

- JDK 17+
- MySQL 8+
- Maven (or the included Maven wrapper)

## Run locally

1. Create a MySQL database:

   ```sql
   CREATE DATABASE LibraryManagementSystem;
   ```

2. Open a terminal in `LibraryManagementSystem/` and set the required environment variables. Example for macOS/Linux:

   ```sh
   export DB_URL='jdbc:mysql://localhost:3306/LibraryManagementSystem'
   export DB_USERNAME='root'
   export DB_PASSWORD='your-local-database-password'
   export JWT_SECRET="$(openssl rand -base64 48)"
   ```

   To enable account-verification email, also set `MAIL_USERNAME` and `MAIL_PASSWORD` to a Gmail account and app password. To create an initial administrator on first startup, optionally set `BOOTSTRAP_ADMIN_EMAIL` and `BOOTSTRAP_ADMIN_PASSWORD` (set both or neither).

3. Start the API:

   ```sh
   ./mvnw spring-boot:run
   ```

   On Windows, use `mvnw.cmd spring-boot:run`.

The application defaults to `jdbc:mysql://localhost:3306/LibraryManagementSystem`. `JPA_DDL_AUTO` defaults to `update` for local development; configure schema migrations and a production-appropriate value before deployment. Credentials and JWT signing keys should be supplied outside the repository.

## API overview

| Area | Route prefix / endpoint | Purpose |
| --- | --- | --- |
| Authentication | `POST /login` | Authenticate a verified user and receive a JWT |
| Account administration | `/admin/**` | Add and deactivate users |
| Catalog and categories | `/librarian/**` | Manage books and categories; search by category or author |
| Borrowing | `/librarian/**` | Borrow and return books, manage fines, and view borrowing history |
| Reports | `/totalBooks`, `/availableBooks`, `/issuedBooks`, and related routes | Catalog, borrowing, and fine statistics |

Most librarian routes require the `LIBRARIAN` role, and `/admin/**` routes require `ADMIN`. Send the token returned by `/login` as `Authorization: Bearer <token>` for protected routes. See the controllers for request parameters and payload shapes.

## API documentation

When the application is running, Swagger UI is available at [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html) and the OpenAPI document at [`http://localhost:8080/v3/api-docs`](http://localhost:8080/v3/api-docs).

## Project layout

Source code is in `LibraryManagementSystem/src/main/java/com/librarymanagementsystem/`:

- `Controller/` — REST endpoints
- `Service/` — application workflows
- `Repository/` — Spring Data persistence interfaces
- `Entity/` and `DTO/` — JPA models and API payloads
- `Security/` — JWT authentication and Spring Security configuration
- `Exception/` — domain exceptions and global error handling

## Development notes

- `application.properties` reads connection details and secrets from environment variables.
- The application creates the `ADMIN` and `LIBRARIAN` roles and starter categories on startup. The bootstrap administrator is created only when both bootstrap environment variables are configured and the account does not already exist.
- Tests are under `LibraryManagementSystem/src/test/java/`.
- Before production use, review authorization on every route, configure database migrations, and provide SMTP, database, and JWT secrets through a secret manager.
