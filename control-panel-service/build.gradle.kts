plugins {
    id("java")
    id("io.quarkus")
}

group = "ru.expanse.quader"
version = "1.0.0-SNAPSHOT"

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
                "${projectDir}/build/classes/java/quarkus-generated-sources/grpc"
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
        "io.quarkus:quarkus-config-yaml"
    )
    compileOnly(
        "org.mapstruct:mapstruct:${properties["mapstructVersion"]}",
        "org.projectlombok:lombok:${properties["lombokVersion"]}",
        project(":lib")
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