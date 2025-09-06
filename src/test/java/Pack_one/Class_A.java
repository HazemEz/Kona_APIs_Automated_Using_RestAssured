package Pack_one;

import SetUp.httpMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Class_A {

    String baseURL = "https://kona-stage-master-fbqzxc.laravel.cloud/api/v1";
    String path = "/booking/pre-requests/data/calendar";

    httpMethods httpMethods = new httpMethods(baseURL,path);

    // Declare Globale Variable to store the response
    Response globalResponse;

    @BeforeMethod (enabled = false)

   public void SetUp (){
        globalResponse = given().get(baseURL + path).then().extract().response();
    }

    @Test
    public void validate_response_code (){

     //   given().get(baseURL + path).then().assertThat().statusCode(500);
      //  globalResponse.then().assertThat().statusCode(200);

        // Declare a local variable to hold the response
        Response returnedResponse;

        // Retrieve the response of the get api and store it in the local variable
        returnedResponse = httpMethods.setupGet();

        // Use the stored value to assert the status code
        returnedResponse.then().statusCode(200);

    }

    @Test
    public void validate_response_code_2 (){
        /*
        Response resp_body = RestAssured.get(baseURL + path);
        Assert.assertEquals(resp_body.statusCode(),200);

         */

        Response returnedResponse = httpMethods.setupGet();
        Assert.assertEquals(returnedResponse.statusCode(),200);
    }

    @Test (enabled = true)
    public void validate_body () {

        /*
        given()
                .get(baseURL + path)
                .then()
                .assertThat()
                .body("status", equalTo("succes")).and()
                .assertThat()
                .body("message",nullValue());
         */

        Response returnedResponse = httpMethods.setupGet();
        returnedResponse.then().assertThat()
                .body("status", equalTo("success")).and()
                .assertThat()
                .body("message",nullValue());
    }

    @Test
    void testCapacityForSpecificDate() {

        String dateToCheck = "2025-08-30";
        int expectedCapacity = 15;
        int expectedCount = 12;
        int expectedRemaining = expectedCapacity - expectedCount;
        int priceIndex = 1;

        // Get the JSON response
       /* String response = given()
                .get(baseURL + path)
                .then()
                .statusCode(200)
                .extract()
                .asString();

        */

        Response returnedResponse = httpMethods.setupGet();

        String stringResponse = returnedResponse.then().extract().asString();

        System.out.println("👉 Testing capacity for date: " + dateToCheck);
        System.out.println("👉 Expected capacity: " + expectedCapacity);
        System.out.println("👉 Expected count: " + expectedCount);
        System.out.println("👉 Expected remaining: " + expectedRemaining);

        // System.out.println("👉 Price index: " + priceIndex);

        // Parse the response JSON
        JsonPath json = new JsonPath(stringResponse);

        // Find the index of the object with matching date
        int index = json.getList("data.date").indexOf(dateToCheck);

        //  System.out.println("👉 Found date index: " + index);
        // Navigate to capacity inside prices[priceIndex]
        int actualCapacity = json.getInt("data[" + index + "].prices[" + priceIndex + "].capacity");
        int actualCount = json.getInt("data[" + index + "].prices[" + priceIndex + "].user_count");
        int actualRemaining = json.getInt("data[" + index + "].prices[" + priceIndex + "].remaining_capacity");

        System.out.println("Actual capacity from response: " + actualCapacity);
        System.out.println("Actual count from response: " + actualCount);
        System.out.println("Actual remaining from response: " + actualRemaining);

        // Assert
        assertThat("Capacity for " + dateToCheck + " should match",
                actualCapacity, equalTo(expectedCapacity));
        assertThat("Capacity for " + dateToCheck + " should match",
                actualCount, equalTo(expectedCount));
        assertThat("Capacity for " + dateToCheck + " should match",
                actualRemaining, equalTo(expectedRemaining));
    }

    @Test
    void ValidateResponseTime (){

        long expectedResponseTime = 1000L;

        /*Response response =
                given()
                        .get(baseURL + path)
                        .then()
                        .extract()
                        .response();
        */

        Response returnedResponse = httpMethods.setupGet();


        System.out.println("The expected response time is " + expectedResponseTime + " ms");
        System.out.println("The actual response time is " + returnedResponse.time() + " ms");

        assertThat("Response time should be under " + expectedResponseTime + " ms",
                returnedResponse.time(), lessThan(expectedResponseTime));

                /*
        given()
                .get(baseURL + path)
                .then()
                .time(lessThan(2300L));*/
    }

    @Test
    void  ValidateNotEmptyArray (){
        /*
        given()
                .get(baseURL + path)
                .then()
                .statusCode(200)
                .body("data", not(empty()));

         */
        Response returnedResponse = httpMethods.setupGet();
        returnedResponse.then().statusCode(200).body("data",not(empty()));

    }

    @Test
    void ValidateInclusionOfWomanDay (){
       /* given()
                .get(baseURL + path)
                .then()
                .statusCode(200)
                .body("data", everyItem(hasKey("is_woman_day")));
                */
        Response returnedResponse = httpMethods.setupGet();
        returnedResponse.then().statusCode(200).body("data",everyItem((hasKey("is_woman_day"))));
    }
}
