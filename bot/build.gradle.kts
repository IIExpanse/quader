plugins {
    id("java")
    id("io.quarkus")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

sourceSets {
    main {
        java {
            srcDirs(
                "${projectDir}/src/main/java",
                "${projectDir}/build/classes/java/quarkus-generated-sources"
            )
        }
    }
    test {
        java {
            srcDirs("${projectDir}/src/test/java")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(
        platform("io.quarkus.platform:quarkus-bom:${properties["quarkusPlatformVersion"]}"),
        "io.quarkus:quarkus-grpc",
        "org.ta4j:ta4j-core:${properties["ta4jVersion"]}"
    )
    compileOnly(
        "ru.tinkoff.piapi:java-sdk-grpc-contract:${properties["tinkoffApiVersion"]}",
        "org.mapstruct:mapstruct:${properties["mapstructVersion"]}",
        "org.projectlombok:lombok:${properties["lombokVersion"]}"
    )
    annotationProcessor(
        "org.projectlombok:lombok:${properties["lombokVersion"]}",
        "org.mapstruct:mapstruct-processor:${properties["mapstructVersion"]}",
        "org.projectlombok:lombok-mapstruct-binding:${properties["lombokMapstructBindingVersion"]}"
    )

    testImplementation(
        "io.quarkus:quarkus-junit5",
        "io.quarkus:quarkus-junit5-mockito"
    )
    testCompileOnly("org.projectlombok:lombok:${properties["lombokVersion"]}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

fun DependencyHandler.implementation(vararg deps: Any) {
    for (dep in deps) {
        add("implementation", dep)
    }
}

fun DependencyHandler.compileOnly(vararg deps: Any) {
    for (dep in deps) {
        add("implementation", dep)
    }
}

fun DependencyHandler.annotationProcessor(vararg deps: Any) {
    for (dep in deps) {
        add("annotationProcessor", dep)
    }
}

fun DependencyHandler.testImplementation(vararg deps: Any) {
    for (dep in deps) {
        add("testImplementation", dep)
    }
}