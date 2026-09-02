plugins {
    id("java")
    id("io.quarkus")
}

group = "ru.expanse.quader"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
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
        platform("io.quarkus.platform:quarkus-bom:${findProp("quarkusPlatformVersion")}"),
        "io.quarkus:quarkus-rest",
        "io.quarkus:quarkus-grpc",
        "io.quarkus:quarkus-config-yaml",
        "org.jboss.logmanager:log4j2-jboss-logmanager"
    )
    compileOnly(
        "org.mapstruct:mapstruct:${findProp("mapstructVersion")}",
        "org.projectlombok:lombok:${findProp("lombokVersion")}",
        project(":lib")
    )
    annotationProcessor(
        "org.projectlombok:lombok:${findProp("lombokVersion")}",
        "org.mapstruct:mapstruct-processor:${findProp("mapstructVersion")}",
        "org.projectlombok:lombok-mapstruct-binding:${findProp("lombokMapstructBindingVersion")}"
    )

    testImplementation(
        "io.quarkus:quarkus-junit5",
        "io.quarkus:quarkus-junit5-mockito"
    )
    testCompileOnly("org.projectlombok:lombok:${findProp("lombokVersion")}")
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

fun findProp(arg: String) : String {
    val res = project.findProperty(arg)
    requireNotNull(res)
    return res.toString()
}
