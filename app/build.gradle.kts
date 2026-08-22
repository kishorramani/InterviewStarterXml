import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.interviewstarter"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.interviewstarter.xml"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    flavorDimensions += "environment"

    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "BASE_URL", "\"https://jsonplaceholder.typicode.com/\"")
        }
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            buildConfigField("String", "BASE_URL", "\"https://jsonplaceholder.typicode.com/\"")
        }
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://jsonplaceholder.typicode.com/\"")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.viewmodel)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    implementation(libs.coil.view)
    implementation(libs.facebook.shimmer)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.websockets)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.work.testing)
}
