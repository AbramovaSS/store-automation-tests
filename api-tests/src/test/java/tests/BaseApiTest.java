package tests;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.example.api.ApiClient;
import org.example.api.RestApiBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;

public class BaseApiTest {

    protected final ApiClient api = new ApiClient();
    private final List<Integer> createdProductIds = new ArrayList<>();

    protected void registerProductForCleanup(Integer productId) {
        if (productId != null) {
            createdProductIds.add(productId);
        }
    }

    @BeforeAll
    static void globalSetup() {
        RestAssured.requestSpecification = RestApiBuilder.getRequestSpec();

        RestAssured.filters(new AllureRestAssured());
    }

    @AfterEach
    void cleanupProducts() {
        for (Integer id : createdProductIds) {
            api.productApiClient.deleteProduct(id);
        }
        createdProductIds.clear();
    }

}
