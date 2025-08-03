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

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
