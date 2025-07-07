# Scene Match Backend

A Java Spring Boot backend for Scene Match. This project uses PostgreSQL as the database and JWT tokens for authentication. It also uses the TMDB API to fetch movie data. The project is designed to be easy to use and deploy. It provides endpoints for user management and movie management.

## Getting Started

1. Clone the repository
2. Create a new PostgreSQL database
3. Create a new user and grant privileges to the database
4. Create a new file called `env.properties` in the root directory of the project and add the following properties:

```
DB_NAME=
DB_USER=
DB_PASSWORD=
JWT_SECRET=
TMDB_API_KEY=
TMDB_API_ACCESS_TOKEN=
ADMIN_PASSWORD = 
ADMIN_USERNAME = 
```

5. Edit the cors configuration in /src/main/java/com/silveredsoul/scenematch/config/SecurityConfig.java to allow requests from the specified origins.
6. Run the application using the following command:

```
mvn spring-boot:run
```

## Endpoints

### Authentication

| Endpoint | Method | Description |
| --- | --- | --- |
| /auth/login | POST | Logs in a user |
| /auth/register | POST | Registers a new user |

### User Management

| Endpoint | Method | Description |
| --- | --- | --- |
| /user/profile | GET | Gets the profile of the current user |
| /user/preferences | GET | Gets the preferences of the current user |
| /user/preferences | PUT | Updates the preferences of the current user |
| /user/preferences | DELETE | Deletes a preference of the current user |
| /user/liked | GET | Gets the liked movies of the current user |
| /user/watched | GET | Gets the watched movies of the current user |
| /user/like/{movieId} | PUT | Likes a movie |
| /user/dislike/{movieId} | PUT | Dislikes a movie |
| /user/watch/{movieId} | PUT | Watches a movie |
| /user/unwatch/{movieId} | PUT | Unwatches a movie |
| /user/recommendations | GET | Gets the recommended movies for the current user |

### Movie Management

| Endpoint | Method | Description |
| --- | --- | --- |
| /movies | GET | Gets all movies |
| /movies/{movieId} | GET | Gets a movie by ID |

## Features

- Authentication
- User Management using JWT tokens for authentication
- Movie Management using TMDB API
