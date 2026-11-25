dependencies {
    implementation(project(":common"))

    implementation("com.codeborne:selenide:7.19.0")
    implementation("org.seleniumhq.selenium:selenium-java:4.49.0")
    implementation("io.qameta.allure:allure-selenide:2.29.0")
}

tasks.register<Test>("smokeUiTest") {
    group = "verification"
    useJUnitPlatform {
        includeTags("smoke")
    }
    systemProperties(System.getProperties().mapKeys { it.key.toString() })
}