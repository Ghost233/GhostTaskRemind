plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.protobuf")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.17.3"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.51.0"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc").option("lite")
            }
            it.builtins {
                create("java").option("lite")
            }
        }
    }
}

android {
    namespace = "me.ghost233.logtool"
    compileSdk = rootProject.extra["COMPILE_SDK_VERSION"] as Int

    defaultConfig {
        minSdk = rootProject.extra["MIN_SDK_VERSION"] as Int

        consumerProguardFiles("consumer-rules.pro")

        externalNativeBuild {
            cmake {
                cppFlags(
                    "-std=c++14",
                    "-frtti",
                    "-fexceptions",
                    "-DANDROID_TOOLCHAIN=clang",
                    "-ffunction-sections",
                    "-fdata-sections",
                    "-Wno-invalid-source-encoding"
                )

                arguments(
                    "-DANDROID_TOOLCHAIN=clang",
                    "-DANDROID_STL=c++_shared",
                    "-DANDROID_CPP_FEATURES=rtti exceptions",
                    "-Wno-invalid-source-encoding"
                )
            }
        }
        
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
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.18.1"
        }
    }
//    应该不受版本影响
//    ndkVersion '21.3.6528147'
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    api("com.google.protobuf:protobuf-javalite:3.17.3")
}