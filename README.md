# Description:
Spring Boot project for ingesting Dota 2 combatlog files and producing match stats.

# How To:
1. Install Maven, Docker and Docker Compose.
2. Build the project: `mvn clean verify`
3. Run: `docker-compose up`
4. Ingest the combatlog file**: 
   `curl -H "Content-Type: text/plain" --data-binary @combatlog_1.txt http://localhost:8080/matches`
5. Get matches: `http://localhost:8080/matches`
6. Get match stats: `http://localhost:8080/matches/{matchId}`

**Combatlog files are located at /src/main/resources/data/

# Swagger:
- `http://localhost:8080/swagger-ui.html#/match-controller`