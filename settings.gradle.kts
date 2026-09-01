rootProject.name = "quader"
include("lib")
include("bot")
include("bot-manager-service")
include("control-panel-service")

pluginManagement {
    plugins {
        id("io.quarkus") version requireNotNull(extra["quarkusPluginVersion"].toString())
    }
}