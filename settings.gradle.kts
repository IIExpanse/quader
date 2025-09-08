rootProject.name = "quader"
include("lib")
include("bot")
include("bot-manager-service")
include("control-panel-service")

pluginManagement {
    val quarkusPluginVersion: String by settings
    plugins {
        id("io.quarkus") version quarkusPluginVersion
    }
}