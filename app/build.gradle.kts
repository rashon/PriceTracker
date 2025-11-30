plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization")
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

    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md" // <-- Added this line
            excludes += "META-INF/licenses/**" // Often good practice to add this as well
        }
    }
}

dependencies {

// --------------------------------------------------------------------------
// Core Kotlin & Android Dependencies
// --------------------------------------------------------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

// --------------------------------------------------------------------------
// Compose Setup & UI
// --------------------------------------------------------------------------
// Compose BOM (Always use platform() for the Bill of Materials)
    implementation(platform(libs.androidx.compose.bom))

// Compose Activity and UI components
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material.icons.extended.android)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

// --------------------------------------------------------------------------
// Navigation
// --------------------------------------------------------------------------
    implementation(libs.androidx.navigation.compose)

// --------------------------------------------------------------------------
// Dependency Injection (Koin)
// --------------------------------------------------------------------------
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

// --------------------------------------------------------------------------
// Networking (Ktor & Serialization)
// --------------------------------------------------------------------------
// Ktor Client Core
    implementation(libs.ktor.client.core)
    // Ktor Android Engine
    implementation(libs.ktor.client.android)
    // Ktor WebSocket Feature
    implementation(libs.ktor.client.websockets)
    implementation(libs.ktor.client.okhttp)
    // Kotlinx Serialization for JSON
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.ktor.serialization.kotlinx.json)

// --------------------------------------------------------------------------
// Testing
// --------------------------------------------------------------------------
// Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test) // For coroutines
    testImplementation(libs.turbine)

// Instrumentation Testing (Android/Compose UI tests)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)


// --------------------------------------------------------------------------
// Debug/Tooling
// --------------------------------------------------------------------------
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}
