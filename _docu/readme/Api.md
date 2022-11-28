
# API

## REST/OpenAPI

### Generate java classes from openapi yaml spec
- in lib module
- openapi-generator-maven-plugin
- openapi yaml files in src/main/resources/openapi
- generate java classes in target/generated-sources/openapi/src/main/java
- run "mvn clean compile"

### Generate openapi yaml spec from java classes
- in backend module
- spring-boot-maven-plugin and
- springdoc-openapi-maven-plugin
- generate openapi.json in target/classes
- run "mvn clean install" (compile is not enough)

### Enable swagger-ui
- in backend module
- springdoc-openapi-ui dependency
- start server
- access http://localhost:8080/swagger-ui.html

### Enrich swagger-ui header with custom info
- in backend module
- OpenAPIConfig.java

### Enrich swagger-ui endpoints with additional information
- in backend module
- PersonController.java
- swagger-annotations dependency
- e.g. @Operation, @Tag, etc.

---

## Kafka

- todo