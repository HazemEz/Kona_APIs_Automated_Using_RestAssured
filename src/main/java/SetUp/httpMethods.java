package SetUp;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;


public class httpMethods {

    String baseURL;
    String path;

    public httpMethods(String baseURL, String path) {
        this.baseURL = baseURL;
        this.path = path;
    }

    public Response setupPost (String Body){

        return      given()
                        .contentType("application/json")
                        .body(Body)
                    .when()
                        .post(baseURL + path)
                    .then()
                        .extract().response();
    }

    public Response setupGet (){

        return      given()
                        .contentType("application/json")
                .when()
                        .get(baseURL + path)
                .then()
                .extract().response();
    }

    public Response setupPut (String Body){

        return      given()
                        .contentType("application/json")
                        .body(Body)
                    .when()
                        .put(baseURL + path)
                    .then()
                        .extract().response();
    }
    public Response setupPatch (String Body){

        return      given()
                        .contentType("application/json")
                        .body(Body)
                    .when()
                        .patch(baseURL + path)
                    .then()
                        .extract().response();
    }

    public Response setupDelete (String Body){

        return      given()
                        .contentType("application/json")
                        .body(Body)
                .when()
                        .delete(baseURL + path)
                .then()
                        .extract().response();
    }



}
