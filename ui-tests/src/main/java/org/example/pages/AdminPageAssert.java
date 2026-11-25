package org.example.pages;

import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class AdminPageAssert extends AbstractAssert<AdminPageAssert, AdminPage> {

    protected AdminPageAssert(AdminPage actual) {

        super(actual, AdminPageAssert.class);
    }

    public static AdminPageAssert assertThat(AdminPage actual) {

        return new AdminPageAssert(actual);
    }

    @Step("Проверить, что появилось уведомление об успешном добавлении товара '{expectedText}'")
    public void hasNotificationWithText(String expectedText) {
        actual.getNotification()
                .shouldBe(visible)
                .shouldHave(text(expectedText));

    }
}
