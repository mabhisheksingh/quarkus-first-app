package org.acme;

import static io.restassured.RestAssured.given;
import static org.acme.common.BaseAPI.V1_BASE_EMP_API_PATH;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
class ExampleResourceIT extends ExampleControllerTest {
    @Test
    void testHelloEndpoint() {
        given()
                .when().get(V1_BASE_EMP_API_PATH + "/public/test")
                .then()
                .statusCode(200)
                .body(is("Hello pass public"));
    }
}
