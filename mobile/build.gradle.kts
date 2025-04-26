@file:Suppress("SpellCheckingInspection")

plugins {
    kotlin("kapt")
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleDaggerHiltAndroid)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.googleDevToolsKsp)
    alias(libs.plugins.compose.compiler)
}

// Version Management
val versionMajor = 2
val versionMinor = 1
val versionPatch = 2
val minimumSdkVersion = 29

fun generateVersionCode(): Int {
    return minimumSdkVersion * 50000000 + versionMajor * 30000 + versionMinor * 300 + versionPatch
}

fun generateVersionName(): String {
    return "${versionMajor}." + "${versionMinor}." + "$versionPatch"
}

android {
    namespace = "com.jackappsdev.password_manager"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jackappsdev.password_manager"
        minSdk = minimumSdkVersion
        targetSdk = 35
        versionCode = generateVersionCode()
        versionName = generateVersionName()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFiles = listOf(rootProject.layout.projectDirectory.file("stability_config.conf"))
}

dependencies {
    // Modules
    implementation(project(":shared"))

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Biometric
    implementation(libs.androidx.biometric)

    // Room DB
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // SQLCipher
    implementation(libs.android.database.sqlcipher)
    implementation(libs.androidx.sqlite)

    // Wearable Services
    implementation(libs.play.services.wearable)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.androidx.wear.remote.interactions)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}
