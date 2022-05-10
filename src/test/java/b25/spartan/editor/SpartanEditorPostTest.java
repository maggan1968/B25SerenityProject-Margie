package b25.spartan.editor;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import utilities.SpartanNewBase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utilities.SpartanUtil;

import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SerenityTest
public class SpartanEditorPostTest extends SpartanNewBase {


    @DisplayName("Editor should be able to POST")
    @Test
    public void postSpartanAsEditor(){

        Map<String, Object> randomSpartanMap = SpartanUtil.getRandomSpartanMap();
        System.out.println("randomSpartanMap = " + randomSpartanMap);


        //send a post request as editor
        given()
                .auth().basic("editor","editor")
                .and()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(randomSpartanMap)
                .log().body()
                .when()
                .post("/spartans")
                .then().log().all();

              //  status code is 201
                Ensure.that("status code is 201",vRes -> vRes.statusCode(201));

              //  content type is Json
                Ensure.that("Content type is Json",vRes -> vRes.contentType(ContentType.JSON));

              //  success message is A Spartan is Born!
                Ensure.that("success message says A Spartan is Born!",vRes -> vRes.body("success", is("A Spartan is Born!")));

              //  id is not null
               Ensure.that("id is not null",vRes -> vRes.body("data.id",Matchers.notNullValue()));

              //  name is correct
              Ensure.that("name is correct",vRes -> vRes.body("data.name",Matchers.is(randomSpartanMap.get("name"))));

              //  gender is correct
              Ensure.that("gender is correct",vRes -> vRes.body("data.gender",Matchers.is(randomSpartanMap.get("gender"))));

              //  phone is correct
              Ensure.that("phone number is correct",vRes -> vRes.body("data.phone",Matchers.is(randomSpartanMap.get("phone"))));

        //                check location header ends with newly generated id

              String id = lastResponse().jsonPath().getString("data.id");

        Ensure.that("check location header ends with newly generated id",
                vRes -> vRes.header("Location", endsWith(id)));

    }


}