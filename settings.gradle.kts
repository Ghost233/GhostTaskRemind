pluginManagement {
    repositories {
        maven("https://mirrors.tuna.tsinghua.edu.cn/flutter/download.flutter.io")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://jitpack.io")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        maven("https://mirrors.tuna.tsinghua.edu.cn/flutter/download.flutter.io")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://jitpack.io")
        google()
        mavenCentral()
    }
}

rootProject.name = "GhostTaskRemind"
include(":app")
include(":LogTool")
include(":GStorage")
include(":GBase")
