package org.acme;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.acme.dto.EmployeeDTO;
import org.acme.model.Employee;
import org.acme.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class ExampleControllerTest {
    @InjectMock
    EmployeeService mockEmployeeService;

    @BeforeEach
    void setUp() {
        Mockito.reset(mockEmployeeService);
    }

    String getEmpStringObj() {
        String empString = """
                            {
                "name":"AbhishekSingh1",
                "age":99,
                "email":"abhshek@gmail.com",
                "password':"Abhsihk@12"
                }
                            """;
        return empString;
    }

    @Test
    public void testPublicEndpoint() {
        given()
                .when().get("/v1/api/emp/public/test")
                .then()
                .statusCode(200)
                .body(is("Hello pass public"));
    }

    @TestSecurity(user = "admin", roles = {"admin"})
    @Test
    public void testCreateEmployeeAsAdmin() throws JsonMappingException, JsonProcessingException {
        var empString = getEmpStringObj();
        ObjectMapper objectMapper = new ObjectMapper();
        EmployeeDTO employee = objectMapper.readValue(empString, EmployeeDTO.class);
        Mockito.when(mockEmployeeService.saveOrUpdate(employee)).thenReturn(1L);

        given()
                .contentType("application/json")
                .body(empString)
                .when().post("/v1/api/emp/create")
                .then()
                .statusCode(200)
                .body(notNullValue());
    }

    @TestSecurity(user = "user", roles = {"user"})
    @Test
    public void testCreateEmployeeAsUser() {
        Employee emp = new Employee();
        emp.setName("Jane Doe");
        emp.setAge(12);
        emp.setEmail("abc@gmail.com");

        given()
                .contentType("application/json")
                .body(emp)
                .when().post("/v1/api/emp/create")
                .then()
                .statusCode(200)
                .body(notNullValue());
    }

    @TestSecurity(user = "admin", roles = {"admin"})
    @Test
    public void testGetAllEmployeesAsAdmin() {
        given()
                .when().get("/v1/api/emp/getAll")
                .then()
                .statusCode(200)
                .body(notNullValue());
    }

    @TestSecurity(user = "user", roles = {"user"})
    @Test
    public void testGetAllEmployeesAsUser() {
        given()
                .when().get("/v1/api/emp/getAll")
                .then()
                .statusCode(403); // Forbidden
    }

    @TestSecurity(user = "user", roles = {"user"})
    @Test
    public void testGetEmployeeByIdAsUser() {
        EmployeeDTO emp = new EmployeeDTO();
        emp.setName("John Doe");
        emp.setAge(12);
        emp.setEmail("abc@gmail.com");
        emp.setId(1L);

        Mockito.when(mockEmployeeService.getEmpId(1L)).thenReturn(emp);

        given()
                .queryParam("id", emp.getId())
                .when().get("/v1/api/emp/getEmpByID")
                .then()
                .statusCode(200)
                .body("name", is("John Doe"))
                .body("position", is("Developer"));
    }

    @TestSecurity(user = "admin", roles = {"admin"})
    @Test
    public void testGetEmployeeByIdAsAdmin() {
        EmployeeDTO emp = new EmployeeDTO();
        emp.setName("John Doe");
        emp.setAge(12);
        emp.setEmail("abc1@gmail.com");
        emp.setId(1L);

        Mockito.when(mockEmployeeService.getEmpId(1L)).thenReturn(emp);

        given()
                .queryParam("id", emp.getId())
                .when().get("/v1/api/emp/getEmpByID")
                .then()
                .statusCode(200)
                .body("name", is("John Doe"));
        // .body("position", is("Developer"));
    }

    @TestSecurity(user = "guest", roles = {"guest"})
    @Test
    public void testUnauthorizedAccess() {
        Employee emp = new Employee();
        emp.setName("John Doe");
        emp.setAge(12);
        emp.setEmail("abc@gmail.com");

        given()
                .contentType("application/json")
                .body(emp)
                .when().post("/v1/api/emp/create")
                .then()
                .statusCode(403); // Forbidden
    }

}