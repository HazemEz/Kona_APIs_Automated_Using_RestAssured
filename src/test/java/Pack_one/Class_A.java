package Pack_one;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Class_A {

    String base_URL = "https://kona-stage-master-fbqzxc.laravel.cloud/api/v1";
    String path = "/booking/pre-requests/data/calendar";

    @Test
    public void validate_response_code (){

        given().get(base_URL + path).then().assertThat().statusCode(500);
    }

    @Test
    public void validate_response_code_2 (){
        Response resp_body = RestAssured.get(base_URL + path);
        Assert.assertEquals(resp_body.statusCode(),200);
    }

    @Test (enabled = true)
    public void validate_body () {

        given()
                .get(base_URL + path)
                .then()
                .assertThat()
                .body("status", equalTo("succes")).and()
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
        String response = given()
                .get(base_URL + path)
                .then()
                .statusCode(200)
                .extract()
                .asString();

        System.out.println("👉 Testing capacity for date: " + dateToCheck);
        System.out.println("👉 Expected capacity: " + expectedCapacity);
        System.out.println("👉 Expected count: " + expectedCount);
        System.out.println("👉 Expected remaining: " + expectedRemaining);

        // System.out.println("👉 Price index: " + priceIndex);

        // Parse the response JSON
        JsonPath json = new JsonPath(response);

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
        Response response =
                given()
                        .get(base_URL + path)
                        .then()
                        .extract()
                        .response();
        System.out.println("The expected response time is " + expectedResponseTime + " ms");
        System.out.println("The actual response time is " + response.time() + " ms");

        assertThat("Response time should be under " + expectedResponseTime + " ms",
                response.time(), lessThan(expectedResponseTime));

                /*
        given()
                .get(base_URL + path)
                .then()
                .time(lessThan(2300L));*/
    }

    @Test
    void  ValidateNotEmptyArray (){
        given()
                .get(base_URL + path)
                .then()
                .statusCode(200)
                .body("data", not(empty()));
    }

    @Test
    void ValidateInclusionOfWomanDay (){
        given()
                .get(base_URL + path)
                .then()
                .statusCode(200)
                .body("data", everyItem(hasKey("is_woman_day")));
    }
}
