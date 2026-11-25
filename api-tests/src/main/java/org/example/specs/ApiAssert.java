package org.example.specs;

import io.qameta.allure.Step;
import org.example.models.PriceValidationErrorResponse;
import org.example.models.ProductItem;
import org.example.models.ProductResponse;
import org.example.models.ProductsListResponse;

import static org.assertj.core.api.Assertions.assertThat;


public class ApiAssert {

    @Step("Проверить, что список товаров пуст")
    public static void assertGoodsListIsEmpty(ProductsListResponse response) {
        assertThat(response.goods())
                .as("Список товаров пуст")
                .isEmpty();
    }

    @Step("Проверить, что список товаров содержит созданный товар")
    public static void assertGoodsListContainsProduct(ProductsListResponse response, ProductItem expectedProduct) {
        assertThat(response.goods())
                .as("Список товаров должен содержать созданный товар")
                .contains(expectedProduct);
    }

    @Step("Проверить, что появляется сообщение об ошибке")
    public static void assertProductPriceIsNotNegative(PriceValidationErrorResponse response) {
        assertThat(response.price())
                .as("Появляется сообщение об ошибке")
                .isEqualTo("Price can not be less, than 0");
    }

    @Step("Проверить, что ID товара соответствует ожидаемому")
    public static void assertProductHasId(ProductItem actualProduct, Integer expectedId) {
        assertThat(actualProduct.id())
                .as("ID товара в теле ответа соответствует искомому")
                .isEqualTo(expectedId);

    }

    @Step("Проверить, что название товара соответствует ожидаемому")
    public static void assertProductHasName(ProductItem actualProduct, String expectedName) {
        assertThat(actualProduct.name())
                .as("Название товара - " + expectedName)
                .isEqualTo(expectedName);
    }

    @Step("Проверить, что цена товара обновилась и соответствует ожидаемой")
    public static void assertProductPriceUpdated(ProductItem actualProduct, Double expectedNewPrice) {
        assertThat(actualProduct.price())
                .as("Цена товара изменилась на " + expectedNewPrice)
                .isEqualTo(expectedNewPrice);
    }

    @Step("Проверить, что появляется сообщение об ошибке")
    public static void assertProductIsNotFound(ProductResponse response, Integer expectedId) {
        assertThat(response.message())
                .as("Появляется сообщение об ошибке")
                .isEqualTo("Good with id '%s' is not found!".formatted(expectedId));

    }

    @Step("Проверить сообщение об ошибке, что товар с ID {expectedId} не найден")
    public static void assertProductNotFoundError(String actualErrorText, Integer expectedId) {
        assertThat(actualErrorText)
                .as("Текст ответа содержит сообщение, что товар не найден")
                .contains("Good with id '%d' is not found".formatted(expectedId));
    }

    @Step("Проверить, что товар с ID {expectedId} успешно удален")
    public static void assertProductIsDeleted(String actualSuccessText, Integer expectedId) {
        assertThat(actualSuccessText)
                .as("Текст ответа содержит сообщение об успешном удалении товара")
                .contains("Good with id '%d' has been deleted successfully!".formatted(expectedId));
    }
}
