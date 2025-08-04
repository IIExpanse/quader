plugins {
    id("java")
    id("io.quarkus") version "3.25.0.CR1"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("io.quarkus.platform:quarkus-bom:" + properties["quarkusPlatformVersion"]))
    implementation("io.quarkus:quarkus-grpc")

    compileOnly("ru.tinkoff.piapi:java-sdk-grpc-contract:" + properties["tinkoffApiVersion"])

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
