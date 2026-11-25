plugins {
    java
    id("io.qameta.allure") version "2.12.0"
}

allprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.qameta.allure")

    allure {
        report {
            version.set("2.29.0")
            singleFile.set(true)
        }
        adapter {
            aspectjWeaver.set(true)
            frameworks {
                junit5 {
                    adapterVersion.set("2.29.0")
                }
            }
        }
    }

    dependencies {
        implementation(files("${rootProject.projectDir}/libs/oa_at_train_project-1.0-SNAPSHOT.jar"))
        implementation("org.assertj:assertj-core:3.27.7")
        implementation("com.github.javafaker:javafaker:1.0.2")
        implementation("com.fasterxml.jackson.core:jackson-databind:2.22.3")
        implementation("org.slf4j:slf4j-simple:2.0.18")

        implementation("io.qameta.allure:allure-junit5:2.29.0")
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    tasks.register("smokeTest") {
        group = "verification"
        dependsOn(":api-tests:smokeApiTest", ":ui-tests:smokeUiTest")
    }
}
