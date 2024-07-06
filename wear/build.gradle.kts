@file:Suppress("SpellCheckingInspection")
import org.gradle.util.internal.GUtil.loadProperties
import java.util.Properties

plugins {
    kotlin("kapt")
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleDaggerHiltAndroid)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.googleDevToolsKsp)
}

// Secrets
val secretsFile = file("secrets.properties")
val secrets: Properties = loadProperties(secretsFile)
val encryptionSecretKey: String = secrets.getProperty("ENCRYPTION_SECRET_KEY")

// Version Management
val versionMajor = 1
val versionMinor = 0
val versionPatch = 0
val minimumSdkVersion = 28

fun generateVersionCode(): Int {
    return minimumSdkVersion * 10000000 + versionMajor * 10000 + versionMinor * 300 + versionPatch
}

fun generateVersionName(): String {
    return "${versionMajor}." + "${versionMinor}." + "$versionPatch"
}

android {
    namespace = "com.jackappsdev.password_manager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jackappsdev.password_manager"
        minSdk = minimumSdkVersion
        targetSdk = 34
        versionCode = generateVersionCode()
        versionName = generateVersionName()
        buildConfigField("String", "ENCRYPTION_SECRET_KEY", encryptionSecretKey)

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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Modules
    implementation(project(":shared"))

    // Core
    implementation(libs.play.services.wearable)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.androidx.wear.remote.interactions)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material.icons.extended)

    // Navigation
    implementation(libs.androidx.compose.navigation)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Room DB
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // SQLCipher
    implementation(libs.android.database.sqlcipher)
    implementation(libs.androidx.sqlite)

    // Horologist
    implementation(libs.horologist.compose.layout)

    // Testing
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}
