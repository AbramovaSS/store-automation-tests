package org.example.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private final SelenideElement buttonAdminPanel = $("[href='/admin']");
    private final SelenideElement buttonOpenCart = $("#open-cart-btn");
    private final SelenideElement makeOrderButton= $("#makeOrder");
    private final SelenideElement notificationOrder = $(".toast");
    private final SelenideElement totalPrice = $("#total-price");

    private final ElementsCollection productCards = $$(".product-card");

    @Step("Нажать на кнопку \"Администрирование\"")
    public MainPage clickButtonAdminPanel() {
        buttonAdminPanel.click();
        return this;
    }

    @Step("Нажать на кнопку \"Корзина\"")
    public MainPage clickButtonOpenCart() {
        buttonOpenCart.click();
        return this;
    }

    @Step("Нажать на кнопку \"Оформить заказ\"")
    public MainPage clickMakeOrderBtn() {
        makeOrderButton.click();
        return this;
    }

    public SelenideElement getProductCardByText(String value) {

        return productCards.findBy(text(value));
    }

    public SelenideElement getNotificationOrder() {

        return notificationOrder;
    }

    public SelenideElement getTotalPrice() {

        return totalPrice;
    }

    @Step("Добавить 3 единицы товара, нажав на кнопку \"В корзину\"")
    public void addToCartThreeTimes(int id) {
        SelenideElement btn = $("#card-" + id + " [data-action='add-to-cart']");
        btn.click();
        btn.click();
        btn.click();
    }

    @Step("Добавить товар с ID = {id}, нажав на кнопку \"В корзину\"")
    public void addToCart(int id) {
        SelenideElement btn = $("#card-" + id + " [data-action='add-to-cart']");
        btn.click();
    }

    @Step("Обновить страницу")
    public MainPage refreshPage() {
        Selenide.refresh();
        return this;
    }
}
