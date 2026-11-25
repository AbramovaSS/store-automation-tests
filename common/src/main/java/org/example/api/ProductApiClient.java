package org.example.api;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.models.ProductRequest;
import org.example.models.ProductResponse;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.BDDAssertions.as;

public class ProductApiClient {

    @Step("Отправить GET-запрос на получение списка товаров")
    public Response getProductsList(int page, int size) {
        return given()
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get("/list");
    }

    @Step("Отправить POST-запрос на добавление товара")
    public Response addProduct(ProductRequest productRequest) {
        return given()
                .body(productRequest)
                .when()
                .post("/add");
    }

    @Step("Отправить GET-запрос на получение товара по ID")
    public Response getItemById(int actualProductId) {
        return given()
                .pathParam("id", actualProductId)
                .when()
                .get("/{id}");

    }

    @Step("Отправить PATCH-запрос на редактирование цены товара")
    public Response updateProduct(int actualProductId, ProductRequest productRequest) {
        return given()
                .pathParam("id", actualProductId)
                .body(productRequest)
                .when()
                .patch("/{id}");

    }

    @Step("Отправить DELETE-запрос на удаление товара")
    public Response deleteProduct(int actualProductId) {
        return given()
                .pathParam("id", actualProductId)
                .delete("/{id}");
    }

    @Step("Создать товар '{name}' с ценой {price} и получить его ID")
    public Integer addProduct(String name, double price) {
        ProductResponse response = addProduct(new ProductRequest(name, price))
                .then()
                .extract()
                .as(ProductResponse.class);

        return response.data().get("id");
    }

}
