plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.pricetracker"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.pricetracker"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

// --------------------------------------------------------------------------
// 1. Core Kotlin & Android Dependencies
// --------------------------------------------------------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

// --------------------------------------------------------------------------
// 2. Compose Setup & UI
// --------------------------------------------------------------------------
// Compose BOM (Always use platform() for the Bill of Materials)
    implementation(platform(libs.androidx.compose.bom))

// Compose Activity and UI components
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

// --------------------------------------------------------------------------
// 3. Navigation
// --------------------------------------------------------------------------
    implementation(libs.androidx.navigation.compose)

// --------------------------------------------------------------------------
// 4. Dependency Injection (Koin)
// --------------------------------------------------------------------------
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)


// --------------------------------------------------------------------------
// 5. Testing
// --------------------------------------------------------------------------
// Unit Testing
    testImplementation(libs.junit)

// Instrumentation Testing (Android/Compose UI tests)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)


// --------------------------------------------------------------------------
// 6. Debug/Tooling
// --------------------------------------------------------------------------
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}