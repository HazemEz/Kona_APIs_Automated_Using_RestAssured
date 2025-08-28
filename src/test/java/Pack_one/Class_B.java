package Pack_one;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.util.function.Predicate.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class Class_B {
    String base_URL = "https://kona-stage-master-fbqzxc.laravel.cloud/api/v1";
    String path = "/user/check-uncompleted-profile";

    String ArrayOfKeys[]= {"phone","phone_code","user_type"};

    String ApprovedPhone = "111";
    String IncompletePhone = "6837688";



    @Test
    public void validate_response_code (){

        // The above string is equavelant to
         String jsonBody = ("{\"phone\":\"111\",\"phone_code\":\"+966\",\"user_type\":\"Sports Guests\"}");

        given()
                .contentType("application/json")
                .body(jsonBody)
        .when()
                .post(base_URL + path)
        .then()
                .statusCode(200);

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
                .post(base_URL + path)
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
                .post(base_URL + path)

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
                .post(base_URL + path)
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
                .post(base_URL + path)
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
                .post(base_URL + path)

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
                .post(base_URL + path)

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
                .post(base_URL + path)

                .then()
                .log().status()
                .statusCode(200);
    }


}
