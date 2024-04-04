plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
}

android {
    namespace = "me.ghost233.ghosttaskremind"
    compileSdk = rootProject.extra["COMPILE_SDK_VERSION"] as Int

    defaultConfig {
        applicationId = "me.ghost233.ghosttaskremind"
        minSdk = rootProject.extra["MIN_SDK_VERSION"] as Int
        targetSdk = rootProject.extra["TARGET_SDK_VERSION"] as Int
        versionCode = 1
        versionName = "1.0"

        ndk {
            abiFilters.add("arm64-v8a")
        }
    }

    signingConfigs {
        create("key") {
            storeFile = file("../key/GhostAndroidKey")
            storePassword = "nopassword123"
            keyAlias = "key0"
            keyPassword = "nopassword123"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("key")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            signingConfig = signingConfigs.getByName("key")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
}

dependencies {
    implementation(project(":GBase"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-analytics:21.5.1")

    implementation("com.github.getActivity:XXPermissions:18.6")
    implementation("com.github.getActivity:EasyWindow:10.6")

    implementation("androidx.room:room-ktx:$2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.databinding:databinding-runtime:8.3.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
}