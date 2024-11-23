package testAPIs;

import models.Products;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class apiTests {

    @Test
    public void getCategories()
    {
        String endpoint = "http://localhost:8888/api_testing/product/read.php";
        var response = given().when().get(endpoint).then();
        response.log().body();

        //Above line does not verify the response. AssertThat needs to be used to verify the response.
        given().when().get(endpoint).then().assertThat().statusCode(200);
    }

    //Get specific item from DB.
    @Test
    public void getProduct()
    {
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        var response = given().queryParam("id",1002).when().get(endpoint).then();
        response.log().body();
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
                "Hiking Backpack",
                "120L all weather travel backpack with rain cover.",
                350,
                3
        );
        var response = given().body(product).when().post(endpoint).then();
        response.log().body();
    }

}
