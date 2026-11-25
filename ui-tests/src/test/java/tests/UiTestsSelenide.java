package tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import org.example.config.ConfigReader;
import org.example.models.ProductRequest;
import org.example.models.ProductResponse;
import org.example.pages.AdminLoginPage;
import org.example.pages.AdminPage;
import org.example.pages.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.Selenide.sleep;
import static io.qameta.allure.SeverityLevel.*;
import static org.example.api.ResponseSpecs.successResponseSpec;
import static org.example.pages.AdminPageAssert.assertThat;
import static org.example.pages.MainPageAssert.assertThat;

@Epic("Интернет-магазин (UI-tests)")
@Owner("Светлана Абрамова")
@Tag("ui")

public class UiTestsSelenide extends BaseUiTest {
    Faker faker = new Faker();

    String product = faker.commerce().productName();
    Double price = faker.number().randomDouble(2, 1, 100);
    String login = ConfigReader.CONFIG.userLogin();
    String password = ConfigReader.CONFIG.userPassword();
    private static final String GLASSES_NAME = "Очки";
    private static final String HAT_NAME = "Шляпа";
    private static final String BLANKET_NAME = "Покрывало";

    private static final double GLASSES_PRICE = 50.50;
    private static final double HAT_PRICE = 60.10;
    private static final double BLANKET_PRICE = 70.10;

    ProductRequest productRequest = new ProductRequest(product, price);
    MainPage mainPage = new MainPage();
    AdminLoginPage adminLoginPage = new AdminLoginPage();
    AdminPage adminPage = new AdminPage();


    @Test
    @Severity(NORMAL)
    @DisplayName("Покупка 3х единиц товара")
    void shouldPurchaseThreeUnitsOfProduct() {
        ProductResponse createResponse = api.productApiClient.addProduct(productRequest)
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductResponse.class);

        Integer actualProductId = createResponse
                .data()
                .get("id");

        createdProductIds.add(actualProductId);

        mainPage.refreshPage()
                .addToCartThreeTimes(actualProductId);

        sleep(2000);

        mainPage.clickButtonOpenCart()
                .clickMakeOrderBtn();

        assertThat(mainPage)
                .hasNotificationOrderWithText("Заказ принят в обработку!");

    }

    @Tag("smoke")
    @Test
    @Severity(CRITICAL)
    @DisplayName("Проверка расчета общей цены")
    void totalPriceCalculationTest() {
        Integer id1 = api.productApiClient.addProduct(GLASSES_NAME, GLASSES_PRICE);
        Integer id2 = api.productApiClient.addProduct(HAT_NAME, HAT_PRICE);
        Integer id3 = api.productApiClient.addProduct(BLANKET_NAME, BLANKET_PRICE);

        createdProductIds.addAll(List.of(id1, id2, id3));

        mainPage.refreshPage();
        mainPage.addToCart(id1);
        mainPage.addToCart(id2);
        mainPage.addToCart(id3);

        double expectedTotalPrice = GLASSES_PRICE + HAT_PRICE + BLANKET_PRICE;

        mainPage.clickButtonOpenCart();

        assertThat(mainPage)
                .verifyTotalPrice(expectedTotalPrice);

    }

    @Test
    @Severity(NORMAL)
    @DisplayName("Проверка уведомления после добавления товара")
    void productAddedNotificationTest() {
        mainPage.clickButtonAdminPanel();
        adminLoginPage.setLogin(login)
                .setPassword(password);
        adminPage.setNameField(product)
                .setPriceField(String.valueOf(price))
                .clickSaveButton();

        assertThat(adminPage)
                .hasNotificationWithText("Товар успешно добавлен!");


    }

    @Tag("smoke")
    @Test
    @Severity(BLOCKER)
    @DisplayName("Редактирование товара")
    void shouldSuccessfullyEditProduct() {
        ProductResponse createResponse = api.productApiClient.addProduct(productRequest)
                .then()
                .spec(successResponseSpec)
                .extract()
                .as(ProductResponse.class);

        Integer actualProductId = createResponse
                .data()
                .get("id");

        createdProductIds.add(actualProductId);

        mainPage.refreshPage()
                .addToCartThreeTimes(actualProductId);

        sleep(2000);

        mainPage.clickButtonAdminPanel();
        adminLoginPage.setLogin(login)
                .setPassword(password);
        adminPage.setInputs(product)
                .clickUpdateButton()
                .clickExitAdminPanelBtn();

        assertThat(mainPage)
                .hasProductCardWithName(product);
    }
}
