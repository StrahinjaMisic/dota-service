## Description:
Spring Boot project for ingesting Dota 2 combatlog files and producing match stats.

## Specs:
- Spring Boot 2.4
- Java 11
- Maven for dependency management and project build
- PostgreSQL 10 as database
- Liquibase for database schema change management
- Hazelcast for distributed caching
- Swagger for API documentation
- JUnit, Mockito and Testcontainers for testing
- Docker for containerization
- Nginx for load balancing

## How To:
1. Install Maven, Docker and Docker Compose.
2. Build the project: `mvn clean verify`
3. Run: `docker-compose up --scale dota=1`
4. Ingest the combatlog file**: `curl -H "Content-Type: text/plain" --data-binary @combatlog_1.txt http://localhost:4000/matches`
5. Get matches: `http://localhost:4000/matches`
6. Get match stats: `http://localhost:4000/matches/{matchId}`
7. Shut down: `docker-compose down`

**Combatlog files are located at /src/main/resources/data/

## Swagger:
- `http://localhost:4000/swagger-ui.html#/match-controller`