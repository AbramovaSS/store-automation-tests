package org.example.pages;

import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;

import static com.codeborne.selenide.Condition.*;

public class MainPageAssert extends AbstractAssert<MainPageAssert, MainPage> {

    protected MainPageAssert(MainPage actual) {
        super(actual, MainPageAssert.class);
    }

    public static MainPageAssert assertThat(MainPage actual) {

        return new MainPageAssert(actual);
    }

    @Step("Проверить, что на витрине добавлен товар '{expectedName}'")
    public void hasProductCardWithName(String expectedName) {
        actual.getProductCardByText(expectedName)
                .shouldBe(visible);

    }

    @Step("Проверить, что появилось уведомление c текстом '{expectedText}'")
    public void hasNotificationOrderWithText(String expectedText) {
        actual.getNotificationOrder()
                .shouldBe(visible)
                .shouldHave(text(expectedText));

    }

    @Step("Проверить, что сумма добавленных в корзину товаров соотвествует общей стоимости '{expectedTotalPrice}'")
    public void verifyTotalPrice(double expectedTotalPrice) {
        actual.getTotalPrice()
                .shouldHave(exactText(String.valueOf(expectedTotalPrice)));
    }

}
