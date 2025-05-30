plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    //SafeArgs
    id("androidx.navigation.safeargs.kotlin")

    //Google Services
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.coroutineretrofit"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.coroutineretrofit"
        minSdk = 24
        targetSdk = 35
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

    buildFeatures{
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {


    val nav_version = "2.9.0"

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Firebase BoM & Analytics
    implementation("com.google.firebase:firebase-bom:33.13.0")
    implementation("com.google.firebase:firebase-analytics-ktx:22.4.0")

    //Authentication
    implementation("com.google.firebase:firebase-auth-ktx:23.2.0")

    //Navigation
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")

    //Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    //Gson Converter
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    //Firebase Cloud Storage
    implementation("com.google.firebase:firebase-storage:21.0.1")

    //Firebase Cloud Firestore
    implementation("com.google.firebase:firebase-firestore:25.1.4")

    //MaterialMotion
    implementation ("com.google.android.material:material:1.12.0")

    //uCrop
    implementation ("com.github.yalantis:ucrop:2.2.10")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

}

