rootProject.name = "quader"
include("bot")
include("lib")

pluginManagement {
    val quarkusPluginVersion: String by settings
    plugins {
        id("io.quarkus") version quarkusPluginVersion
    }
}