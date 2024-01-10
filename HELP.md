# Word Counter Project

This Spring Boot application features a Word Counter service that leverages Apache Spark for efficient data processing. 
The project is container-ready with Docker support and integrates Google's translation API for multilingual word counting.

## Features

- Word counting service utilizing Apache Spark.
- Spring Boot framework for straightforward project setup and management.
- Docker integration for easy deployment and container management.
- Filters out non-alphabetic characters and considers translated equivalents of words.
- Utilizes Google API Translator for English translations.
- Concurrency support for multithreaded environments.
- Microservice architecture with external endpoints.

## API Endpoints

- `POST /api/v1/wordcount`: Add text and count words.
- `GET /api/v1/translate`: Retrieve translated text in English.

## Requirements

- Java 8 or 11
- Maven (for project building)
- Apache Spark
- Docker (optional, for containerization)

# NotStopWord Utility Class

The `NotStopWord` class is a utility in the Word Counter project that helps in identifying valid words for counting. This class filters out common stop words and ensures that only words with alphabetic characters are considered.

## Features

- **Stop Word Filtering:** Filters out common English stop words.
- **Alphabetic Character Check:** Ensures that words consist only of alphabetic characters, ignoring any numbers or special characters. 
- Such as "is", "are", ".", "if", "the", "in", "of", "and", "a", "an"
- **Static Utility Usage:** No need to instantiate the class; directly use the static method.

## Usage

To use the `NotStopWord` utility, simply call the `isNotStopWord` method with a string argument. The method returns `true` if the word is not a stop word and consists only of alphabetic characters.

```java
boolean isValid = NotStopWord.isNotStopWord("example");
```
## Installation & Running

Ensure local Swagger API is accessible at:
[Swagger UI](http://localhost:9090/swagger-ui/index.html)

### Building the Application

Using Maven, build the project with:

```
mvn clean install
```
###Run the application:
```
java -jar target/wordcounter-0.0.1-SNAPSHOT.jar
```
##Docker Setup
####Build the Docker image:
```
docker build -t wordcounter-app .
docker run -p 9090:9090 wordcounter-app
```
### Testing

This project uses JUnit 5 and Mockito for unit testing. 
Tests are designed to ensure the accuracy and efficiency of the word counting logic,
including its interaction with external services like the Translator API.

To run the tests, use the following Maven command:
```
mvn test
```