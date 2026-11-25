package tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import org.example.models.*;
import org.example.specs.ApiAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.SeverityLevel.*;
import static org.example.api.ResponseSpecs.*;


@Epic("Каталог товаров (API-tests)")
@Feature("CRUD-операции с товарами")
@Tag("api")
@Owner("Светлана Абрамова")

public class StoreApiTest extends BaseApiTest {

    Faker faker = new Faker();

    String product = faker.commerce().productName();
    Double price = faker.number().randomDouble(2, 1, 100);
    private static final int NON_EXISTENT_ID = 0;
    private static final double UPDATED_PRICE = 50.0;
    private static final double NEGATIVE_PRICE = -1.00;

    ProductRequest productRequest = new ProductRequest(product, price);


    @Test
    @Severity(NORMAL)
    @DisplayName("Запрос пустого списка товаров")
    void requestForListOfGoodsGiven() {
        ProductsListResponse listResponse = api.productApiClient.getProductsList(0, 100)
        .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductsListResponse.class);

        ApiAssert.assertGoodsListIsEmpty(listResponse);

    }

    @Tag("smoke")
    @Test
    @Severity(BLOCKER)
    @DisplayName("Добавление товара с проверкой, что он вернулся в списке")
    void addAndVerifyProductAssertJ() {
        ProductResponse createResponse = api.productApiClient.addProduct(productRequest)
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductResponse.class);

        Integer actualProductId = createResponse
                .data()
                .get("id");

        ProductsListResponse listResponse = api.productApiClient.getProductsList(0, 100)
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductsListResponse.class);

        ProductItem expectedProduct = new ProductItem(
                actualProductId,
                productRequest.name(),
                productRequest.price());

        ApiAssert.assertGoodsListContainsProduct(listResponse, expectedProduct);
        registerProductForCleanup(actualProductId);
    }

    @Test
    @Severity(NORMAL)
    @DisplayName("Добавление товара с отрицательной ценой")
    void addProduct400() {
        ProductRequest badPriceRequest = new ProductRequest(productRequest.name(), NEGATIVE_PRICE);

        PriceValidationErrorResponse errorMessage = api.productApiClient.addProduct(badPriceRequest)
                .then()
                .spec(errorResponseSpec)
                .extract()
                .as(PriceValidationErrorResponse.class);

        ApiAssert.assertProductPriceIsNotNegative(errorMessage);
    }

    @Tag("smoke")
    @Test
    @Severity(BLOCKER)
    @DisplayName("Получение товара по ID")
    void findProductById() {
        ProductResponse createResponse = api.productApiClient.addProduct(productRequest)
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductResponse.class);

        Integer createdProductId = createResponse
                .data()
                .get("id");

        ProductItem actualProduct = api.productApiClient.getItemById(createdProductId)
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductItem.class);

        ApiAssert.assertProductHasId(actualProduct, createdProductId);
        ApiAssert.assertProductHasName(actualProduct, productRequest.name());

        registerProductForCleanup(createdProductId);
    }

    @Test
    @Severity(NORMAL)
    @DisplayName("Товар не найден по ID")
    void productNotFoundById() {
        ProductResponse notFoundResponse = api.productApiClient.getItemById(NON_EXISTENT_ID)
                .then()
                .spec(notFoundResponseSpec)
                .extract()
                .as(ProductResponse.class);

        ApiAssert.assertProductIsNotFound(notFoundResponse, NON_EXISTENT_ID);
    }

    @Tag("smoke")
    @Severity(BLOCKER)
    @Test
    @DisplayName("Редактирование цены товара")
    void updateProduct() {
        ProductRequest newPriceRequest = new ProductRequest(productRequest.name(), UPDATED_PRICE);

        ProductResponse createResponse = api.productApiClient.addProduct(productRequest)
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductResponse.class);

        Integer createdProductId = createResponse
                .data()
                .get("id");

        ProductItem actualProduct = api.productApiClient.updateProduct(createdProductId, newPriceRequest)
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductItem.class);

        ApiAssert.assertProductHasId(actualProduct, createdProductId);
        ApiAssert.assertProductHasName(actualProduct, productRequest.name());
        ApiAssert.assertProductPriceUpdated(actualProduct, UPDATED_PRICE);

        registerProductForCleanup(createdProductId);

    }

    @Test
    @Severity(NORMAL)
    @DisplayName("Попытка установить отрицательную цену товару")
    void updateProduct400() {
        ProductRequest badProductRequest = new ProductRequest(productRequest.name(), NEGATIVE_PRICE);

        ProductResponse createResponse = api.productApiClient.addProduct(productRequest)
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductResponse.class);

        Integer actualProductId = createResponse
                .data()
                .get("id");

        PriceValidationErrorResponse errorMessage = api.productApiClient.updateProduct(actualProductId, badProductRequest)
                .then()
                .spec(errorResponseSpec)
                .extract()
                .as(PriceValidationErrorResponse.class);

        ApiAssert.assertProductPriceIsNotNegative(errorMessage);

        registerProductForCleanup(actualProductId);

    }

    @Test
    @Severity(NORMAL)
    @DisplayName("Товар не найден при редактировании")
    void productNotFoundDuringUpdate() {
        String errorText = api.productApiClient.updateProduct(NON_EXISTENT_ID, productRequest)
                .then()
                .spec(notFoundResponseSpec)
                .extract()
                .asString();

        ApiAssert.assertProductNotFoundError(errorText, NON_EXISTENT_ID);
    }

    @Tag("smoke")
    @Severity(BLOCKER)
    @Test
    @DisplayName("Успешное удаление товара")
    void removeItem() {
        ProductResponse createResponse = api.productApiClient.addProduct(productRequest)
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductResponse.class);

        Integer actualProductId = createResponse
                .data()
                .get("id");

        String successText = api.productApiClient.deleteProduct(actualProductId)
                .then()
                .spec(successResponseSpec)
                .extract()
                .asString();

        ApiAssert.assertProductIsDeleted(successText, actualProductId);
    }

    @Test
    @Severity(NORMAL)
    @DisplayName("Попытка удаления несуществующего товара")
    void removeItem404() {
        String errorText = api.productApiClient.deleteProduct(NON_EXISTENT_ID)
                .then()
                .spec(notFoundResponseSpec)
                .extract()
                .asString();

        ApiAssert.assertProductNotFoundError(errorText, NON_EXISTENT_ID);

    }

}
