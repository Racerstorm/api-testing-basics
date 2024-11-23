package testAPIs;

import models.Products;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class apiResponseTests {

    @Test
    public void getCategories()
    {
        String endpoint = "http://localhost:8888/api_testing/product/read.php";
        var response = given().when().get(endpoint).then();
        //response.log().body();

        //Print the header information in the response.
        given().when().get(endpoint).then().log().headers();

        //Verifying the header strucutre in the response.
        given().when().get(endpoint).then().log()
                .headers()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", equalTo("application/json; charset=UTF-8"));


        //Above line does not verify the response. AssertThat needs to be used to verify the response.
        given().when().get(endpoint).then().assertThat().statusCode(200);

        //Additional assertions on the response body
        given().when().get(endpoint).then()
                .assertThat()
                .statusCode(200)
                .body("records.size()",greaterThan(0)) //Records.size gets the size of the list and ensures at least 1 product exists in the DB
                .body("records.id",everyItem(notNullValue())) //Gets the list of IDs and checks that each ID in the list is not null
                .body("records.name",everyItem(notNullValue())) //Gets the list of names in the db and checks that each ID in the list is not null
                .body("records.description",everyItem(notNullValue())) //Gets the list of description in the db and checks that each ID in the list is not null
                .body("records.price",everyItem(notNullValue())) //Gets the list of prices in the db and checks that each ID in the list is not null
                .body("records.category_id",everyItem(notNullValue())) //Gets the list of category IDs in the db and checks that each ID in the list is not null
                .body("records.category_name",everyItem(notNullValue())); //Gets the list of category names in the db and checks that each ID in the list is not null

        //Verify specific record in the response
        given().when().get(endpoint).then()
                .assertThat()
                .statusCode(200)
                .body("records.name[0]",equalTo("Sweatband"));



    }

    //Chaining multiple methods to verify the response structure.
    @Test
    public void getProduct()
    {
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        var response = given().queryParam("id",1002)
                .when()
                .get(endpoint)
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo("1002"))
                .body("name", equalTo("Sweatband"))
                .body("description", equalTo("Sports sweatband for athletes."))
                .body("price", equalTo("6.00"));
    }

    //Deserialization of response
    @Test
    public void getDeserializedProduct()
    {
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        given().queryParam("id",1002)
                .when()
                .get(endpoint)
                .as(Products.class); //This part takes the response from the GET request and deserializes it into a Java object of type Products. This means the JSON or XML response body will be converted into a Products object, making it easier to work with in your tests.


        //Add assertions to the deserialized java object of the type 'Products'
        Products expectedproduct = new Products(
                1002,
                "Sweatband",
                "Sports sweatband for athletes.",
                6.00,
                3,
                "Active Wear - Unisex"
        );
        Products actualproduct = given().queryParam("id",1002).when().get(endpoint).as(Products.class);
        //In order to compare the values in the two products objects, the samePropertyValuesAs method needs to be used. EqualsTo method would compare the memory locations, and not the values.
        assertThat(actualproduct,samePropertyValuesAs(expectedproduct));
    }

    //Add item to DB
    @Test
    public void createproduct()
    {
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        String body = """
                {
                "name" : "Travel backpack",
                "description" : "55L all weather travel backpack with rain cover.",
                "price" : "100",
                "category_id" : "3"
                }
                """;
        var response = given().body(body).when().post(endpoint).then();
        response.log().body();
    }

    //Update existing item in DB.
    @Test
    public void updateProduct()
    {
        String endpoint = "http://localhost:8888/api_testing/product/update.php";
        //Price is the only parameter being modified. Rest left as is.
        String body = """
                {
                "id": "1000",
                "name" : "Travel backpack",
                "description" : "55L all weather travel backpack with rain cover.",
                "price" : "150",
                "category_id" : "3"
                }
                """;
        var response = given().body(body).when().put(endpoint).then();
        response.log().body();
    }

    //Delete an item from DB.
    @Test
    public void deleteProduct()
    {
        String endpoint = "http://localhost:8888/api_testing/product/delete.php";
        String body = """
                {
                "id":"1000"
                }
                """;
        var response = given().body(body).when().delete(endpoint).then();
        response.log().body();
    }

    @Test
    public void createSerializedProduct()
    {
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        Products product = new Products(
                "Travel Backpack",
                "55L all weather travel backpack with rain cover.",
                150,
                3
        );
        var response = given().body(product).when().post(endpoint).then();
        response.log().body();
    }

}
