
This is an example of Spring Boot Web App called "Student Result Management System" (assignment).

## Pre-requisites:
 * Java 17+
 * PostgreSQL (v9.6+)

To initialize the database, please run `setup/setup-db.sh`

## To start the application:

`./mvnw spring-boot:run`

The application should now be available on `http://localhost:8080`.


## To use the application:

The OpenAPI UI is available at `http://localhost:8080/swagger-ui.html` (JSON at `http://localhost:8080/api-docs`)
The logs are available in the `logs/` directory (see `src/main/resources/logback.xml` for logs config)

## To run the tests:

`./mvnw install test`

The code coverage report is then available in `target/site/jacoco/index.html`

## Notes on API authentication

Some operations on the API (HTTP POST/DELETE) reuire a token. The expected token can be set via the environment variable `SRMS_TOKEN` before starting the app:

`export SRMS_TOKEN=secret-token-here`

## Things that could/should to be improved
  * Use h2 in-memory DB for the UnitTests to test SQL requests, or an actual Postgre DB dedicated to the tests
  * Use maven profiles to provide a "dev" profile for local devs, and a "production" profile for QA/Staging/Production environments (e.g. logs configuration, token, databse config, ...)
  * Add more Integration tests (see an example in `src/test/java/io/shyftlabs/srms/StudentResultManagementApplicationTests.java`), for example to make sure a result is removed when the associated student/course is removed
  * Improve code coverage
