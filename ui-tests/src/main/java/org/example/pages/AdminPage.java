package org.example.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AdminPage {
    private final ElementsCollection inputs = $$("[style='width:150px']");
    private final ElementsCollection updateButton = $$("[data-action='update']");
    private final SelenideElement exitAdminPanelBtn = $("[href='/']");
    private final SelenideElement nameField = $("#n-name");
    private final SelenideElement priceField =  $("#n-price");
    private final SelenideElement saveButton = $("#add-btn");
    private final SelenideElement notification = $(".toast");

    @Step("Изменить наименование товара \"{value}\"")
    public AdminPage setInputs(String value) {
        inputs.get(0).setValue(value);
        return this;
    }

    @Step("Нажать на кнопку \"Сохранить\"")
    public AdminPage clickUpdateButton() {
        updateButton.get(0).click();
        return this;
    }

    @Step("Нажать на кнопку \"Вернуться на сайт\"")
    public AdminPage clickExitAdminPanelBtn() {
        exitAdminPanelBtn.click();
        return this;
    }

    @Step("Ввести наименование товара \"{value}\"")
    public AdminPage setNameField(String value) {
        nameField.shouldBe(visible).setValue(value);
        return this;
    }

    @Step("Ввести стоимость товара \"{value}\"")
    public AdminPage setPriceField(String value) {
        priceField.setValue(String.valueOf(value));
        return this;
    }

    @Step("Нажать на кнопку \"Создать\"")
    public AdminPage clickSaveButton() {
        saveButton.click();
        return this;
    }

    public SelenideElement getNotification() {
        return notification;
    }

}
