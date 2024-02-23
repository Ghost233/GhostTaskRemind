// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra["MIN_SDK_VERSION"] = 30
    extra["TARGET_SDK_VERSION"] = 34
    extra["COMPILE_SDK_VERSION"] = 34

    dependencies {
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath("com.google.firebase:perf-plugin:1.4.2")
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.9.0")
    }
}

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.android.library") version "8.2.2" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}