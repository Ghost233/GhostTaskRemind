plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "me.ghost233.gbase"
    compileSdk = rootProject.extra["COMPILE_SDK_VERSION"] as Int

    defaultConfig {
        minSdk = rootProject.extra["MIN_SDK_VERSION"] as Int

        consumerProguardFiles("consumer-rules.pro")

        ndk {
            abiFilters.add("arm64-v8a")
        }
    }

    buildTypes {
        release {
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
}

dependencies {
    api(project(":LogTool"))
    api(project(":GStorage"))

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    api("com.blankj:utilcodex:1.31.1")
    api("io.github.cymchad:BaseRecyclerViewAdapterHelper4:4.1.4")
}
