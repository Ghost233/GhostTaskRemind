# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

message(">>> ANDROID_ABI: ${ANDROID_ABI}")

project("logtool")

set(sdkproject "${CMAKE_SOURCE_DIR}/")

file(GLOB JNI_SRC_LIST ${sdkproject}/*.cpp)
file(GLOB CPP_SRC_LIST ${sdkproject}/src/*.cpp)

message("JNI_SRC_LIST: ${JNI_SRC_LIST}")
message("CPP_SRC_LIST: ${CPP_SRC_LIST}")

include_directories(AFTER "${sdkproject}/")
include_directories(AFTER "${sdkproject}/src")

get_property(dirs DIRECTORY ${CMAKE_SOURCE_DIR} PROPERTY INCLUDE_DIRECTORIES)
message(">>> include_dirs=${dirs} ") #打印一下目录情况

add_library(logtool
        SHARED

        ${CPP_SRC_LIST}
        ${JNI_SRC_LIST}
        )

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

add_definitions(-DUITESTDEBUG=${UITESTDEBUG}) #!necessary