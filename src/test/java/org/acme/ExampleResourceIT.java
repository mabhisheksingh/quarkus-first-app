package org.acme;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
class ExampleResourceIT extends ExampleControllerTest {
    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/v1/api/emp/public/test")
                .then()
                .statusCode(200)
                .body(is("Hello pass public"));
    }
}
