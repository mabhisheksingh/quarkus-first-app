# code-with-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

if u have maven installed You can run your application in dev mode that enables live coding using:

```shell script
mvn compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- JDBC Driver - H2 ([guide](https://quarkus.io/guides/datasource)): Connect to the H2 database via JDBC
- YAML Configuration ([guide](https://quarkus.io/guides/config-yaml)): Use YAML to configure your Quarkus application
- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing Jakarta REST and
  more

## Provided Code

### YAML Config

Configure your application with YAML

[Related guide section...](https://quarkus.io/guides/config-reference#configuration-examples)

The Quarkus application configuration is located in `src/main/resources/application.yml`.

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)

## Docker compose file

In docker compose file added two service postgres and keycloak both will use docker and we are setting postgres sql as
backend for DB for keycloak

Once u run docker compose file then u should login in http://localhost:8080/ and create new
relam [Relam JSON data](/keycloak/quarkus-auth-poc.json)

## Notes

- Dev Ui URL for debugging and other purposes

```text
http://localhost:9001/q/dev-ui/endpoints
```

- Swagger UI URL
```text
http://localhost:9001/q/swagger-ui/
```

### Maven dependency for swagger

```xml
    <!--Swagger -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-openapi</artifactId>
</dependency>
```

- **quarkus.oidc.application-type=web-app/service**

```txt
The quarkus.oidc.application-type configuration property in Quarkus is used to specify the type of OIDC (OpenID Connect) application you are developing. This helps Quarkus configure the appropriate authentication and authorization mechanisms for your application. The two primary types are web-app and service.

quarkus.oidc.application-type Values
web-app:

This is used for traditional web applications where the user interacts with the application through a web browser.
The OIDC protocol flow typically involves redirecting the user to the Keycloak login page for authentication.
After successful authentication, Keycloak redirects the user back to the application with the authorization code, which is then exchanged for tokens.
Sessions and cookies are commonly used to manage user authentication state.
service:

This is used for backend services or APIs that do not have direct user interaction through a browser.
In this scenario, the application is often a resource server that validates tokens received in API requests.
Direct access grants (Resource Owner Password Credentials Grant) or client credentials grant type are often used.
There is no need for browser redirects or managing user sessions with cookies.
When to Use Each Type
Web Application (web-app):

Suitable for applications with a user interface accessed via a web browser.
Example: An online shopping site, a social media platform, or any web-based dashboard.
Service (service):

Suitable for backend services, microservices, or APIs that are consumed by other services or applications.
Example: A RESTful API providing data to a mobile app, or a backend microservice in a microservices architecture.

```

### For Authroization

We need to create 2 user in keycloak relam and assign a role from client so that their access token can use as a role based accesed in this application for this example i have created two user abhishekuser(assign role user) and abhishek(assigned role user)