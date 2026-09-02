plugins {
    id("java-library")
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
        platform("io.quarkus.platform:quarkus-bom:${findProp("quarkusPlatformVersion")}")
    )
}

// todo: make this shared, remove duplicates
fun findProp(arg: String) : String {
    val res = project.findProperty(arg)
    requireNotNull(res)
    return res.toString()
}
