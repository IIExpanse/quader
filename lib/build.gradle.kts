plugins {
    id("java-library")
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
                "${projectDir}/src/main/java"
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
        platform("io.quarkus.platform:quarkus-bom:${properties["quarkusPlatformVersion"]}")
    )
}