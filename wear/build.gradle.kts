plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

// Version Management
val versionMajor = 1
val versionMinor = 2
val versionPatch = 1
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

        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core
    implementation(libs.play.services.wearable)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.ui.tooling.preview)

    // Testing
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
