package Pack_one;

import SetUp.httpMethods;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class Class_B {

    String baseURL = "https://kona-stage-master-fbqzxc.laravel.cloud/api/v1";
    String path = "/user/check-uncompleted-profile";

    httpMethods httpMethods = new httpMethods(baseURL,path);

    String ArrayOfKeys[]= {"phone","phone_code","user_type"};


    String ApprovedPhone = "111";
    String IncompletePhone = "6837688";


    @BeforeMethod (enabled = false)

    // This is a method returns a response of the passed body
    Response Setup_POST (String Body){

        return      given()
                        .contentType("application/json")
                        .body(Body)
                    .when()
                        .post(baseURL + path)
                    .then()
                        .extract().response();
    }

    @Test
    public void validate_response_code (){

        // Define the body passed to the setup post method
         String jsonBody =     ("{\"phone\":\"111\",\"phone_code\":\"+966\",\"user_type\":\"Sports Guests\"}");

         // Declare a local variable to hold the response
        Response returnedResponse ;

         // Retrieve the response of the post api and store it in the local variable
        returnedResponse = httpMethods.setupPost(jsonBody);

        // Use the stored value to assert the status code
        returnedResponse.then().statusCode(200);


      //  returnedResponse = Setup_POST(jsonBody);

      /*  given()
                .contentType("application/json")
                .body(jsonBody)
        .when()
                .post(baseURL + path)
        .then()*/
        //        returnedResponse.then().statusCode(200);

        /*        Map<String, Object> body = new HashMap<>();
        body.put("phone", "111");
        body.put("phone_code", "+966");
        body.put("user_type", "Sports Guests");
*/

        //    String ArrayOfTestData[]={"111","+966","Sports Guests"};

      /*  String jsonBody = "{"   + ArrayOfKeys[0] + ":" + ArrayOfTestData[0] + ","
                                + ArrayOfKeys[1] + ":" + ArrayOfTestData[1] + ","
                                + ArrayOfKeys[2] + ":" + ArrayOfTestData[2] + "}";*/
    }



    @Test
    public void validate_response_body (){

        Map<String, Object> body = new HashMap<>();
        body.put("phone", ApprovedPhone);
        body.put("phone_code", "+966");
        body.put("user_type", "Sports Guests");

        given()
                .contentType("application/json")
                .body(body)
        .when()
                .post(baseURL + path)
        .then()
                .assertThat().body("status", equalTo("success"));
    }

    @Test
    public void validate_inclusion_of_key (){

        Map<String, Object> body = new HashMap<>();
        body.put("phone", "111");
        body.put("phone_code", "+966");
        body.put("user_type", "Sports Guests");

        given()
                .contentType("application/json")
                .body(body)

        .when()
                .post(baseURL + path)

        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .assertThat().body("message",hasKey("need_complete"));
    }


    @Test
    public void validate_response_body_for_approved_profile (){

        Map<String, Object> body = new HashMap<>();
        body.put("phone", "111");
        body.put("phone_code", "+966");
        body.put("user_type", "Sports Guests");

        given()
                .contentType("application/json")
                .body(body)
        .when()
                .post(baseURL + path)
        .then()
                .assertThat().body("message.need_complete", equalTo(false) );
    }

    @Test
    public void validate_response_body_for_incomplete_profile (){

        Map<String, Object> body = new HashMap<>();
        body.put("phone", IncompletePhone);
        body.put("phone_code", "+966");
        body.put("user_type", "Sports Guests");

        given()
                .contentType("application/json")
                .body(body)
        .when()
                .post(baseURL + path)
        .then().log().body()
                .assertThat().body("message.need_complete", equalTo(true) );
    }

    @Test
    public void validate_required_phone_key (){

        Map<String, Object> body = new HashMap<>();
     //   body.put("phone", "111");
        body.put("phone_code", "+966");
        body.put("user_type", "Sports Guests");

        given()
                .contentType("application/json")
                .body(body)
        .when()
                .post(baseURL + path)

        .then().log().status()
                .statusCode(Matchers.not(200));
    }

    @Test
    public void validate_required_phone_code_key (){

        Map<String, Object> body = new HashMap<>();
        body.put("phone", "111");
      //  body.put("phone_code", "+966");
        body.put("user_type", "Sports Guests");

        given()
                .contentType("application/json")
                .body(body)

        .when()
                .post(baseURL + path)

        .then()
                .log().status()
                .statusCode(Matchers.not(200));
    }
    @Test
    public void validate_not_required_type_key (){

        Map<String, Object> body = new HashMap<>();
        body.put("phone", "111");
        body.put("phone_code", "+966");
        //body.put("user_type", "Sports Guests");

        given()
                .contentType("application/json")
                .body(body)

                .when()
                .post(baseURL + path)

                .then()
                .log().status()
                .statusCode(200);
    }


}
