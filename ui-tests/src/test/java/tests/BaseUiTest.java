package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.codeborne.selenide.logevents.SimpleReport;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.example.api.ApiClient;
import org.example.api.RestApiBuilder;
import org.example.config.ConfigReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BaseUiTest {

    private static final Logger log = LoggerFactory.getLogger(BaseUiTest.class);

    protected RequestSpecification requestSpec;

    protected final ApiClient api = new ApiClient();

    protected List<Integer> createdProductIds = new ArrayList<>();

    private final SimpleReport report = new SimpleReport();

    @BeforeAll
    static void configureSelenide() {
        log.info("Браузер: {}", ConfigReader.CONFIG.browserName());
        log.info("Размер окна: {}", ConfigReader.CONFIG.browserSize());
        log.info("Базовый URL стенда: {}", ConfigReader.CONFIG.baseUrl());
        log.info("Таймаут: {}", ConfigReader.CONFIG.timeout());
        log.info("Подробное логирование: {}", ConfigReader.CONFIG.enableDetailedLogging());

        Configuration.baseUrl = ConfigReader.CONFIG.baseUrl();
        Configuration.browser = ConfigReader.CONFIG.browserName();
        Configuration.browserSize = ConfigReader.CONFIG.browserSize();
        Configuration.timeout = ConfigReader.CONFIG.timeout();

        if (!SelenideLogger.hasListener("AllureSelenide")) {
            SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        }
        RestAssured.filters(new AllureRestAssured());

        RestAssured.requestSpecification = RestApiBuilder.getRequestSpec();

    }

    @BeforeEach
    void setup() {

        if (ConfigReader.CONFIG.enableDetailedLogging()) {
            report.start();
        }

        this.requestSpec = RestApiBuilder.getRequestSpec();

        Selenide.open("/");
    }


    @AfterEach
    void tearDown() {
        try {
            for (Integer id : createdProductIds) {
                api.productApiClient.deleteProduct(id);
            }

            if (ConfigReader.CONFIG.enableDetailedLogging()) {
                report.finish("Пошаговый отчет");
            }
        } finally {
            createdProductIds.clear();
            closeBrowser();
        }
    }

    private void closeBrowser() {
        Selenide.closeWebDriver();
    }
}