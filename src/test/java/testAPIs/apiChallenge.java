package testAPIs;
import models.Products;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

public class apiChallenge {

    @Test
    public void createnewProduct()
    {
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        Products product = new Products(
                "Sweatband",
                "Sports sweatband for athletes.",
                5,
                3
        );
        var response = given().body(product).when().post(endpoint).then();
        response.log().body();
    }

    @Test
    public void updateProduct()
    {
        String endpoint = "http://localhost:8888/api_testing/product/update.php";
        Products product = new Products(
                1002,
                "Sweatband",
                "Sports sweatband for athletes.",
                6,
                3,
                "Active Wear - Unisex"
        );
        var response = given().body(product).when().put(endpoint).then();
        response.log().body();
    }

    @Test
    public void getProductDetails()
    {
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        var response = given().queryParam("id",1002).when().get(endpoint).then();
        response.log().body();
    }


    //API Response validation challenge
    @Test
    public void APIResponseChallenge()
    {
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        given().queryParam("id",18)
                .when()
                .get(endpoint)
                .then()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", equalTo("application/json"))
                .body("id", equalTo("18"))
                .body("name", containsStringIgnoringCase("Multi-Vitamin"))
                .body("description", equalTo("A daily dose of our Multi-Vitamins fulfills a day’s nutritional needs for over 12 vitamins and minerals."))
                .body("price", equalTo("10.00"))
                .body("category_id", equalTo(4))
                .body("category_name", equalTo("Supplements"));


    }
}
