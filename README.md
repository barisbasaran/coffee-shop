# Coffee Shop

## About 

This is an example **Java** RESTful web service for a **coffee shop**.

## Setup

### Build project

Run `mvn package` to build project with _Maven_.

### Start application

Run `docker-compose --profile local up` to start application with _Docker_.

To check that your application is running enter url `http://localhost:8080/`

You may see application's health at `http://localhost:8081/healthcheck`

## Architecture

![Architecture](docs/CQRS%20Example.jpg)

