dependencies {
    implementation(project(":common"))
}

tasks.register<Test>("smokeApiTest") {
    group = "verification"
    useJUnitPlatform {
        includeTags("smoke")
    }
    systemProperties(System.getProperties().mapKeys { it.key.toString() })
}
