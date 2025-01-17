plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    //SafeArgs
    alias(libs.plugins.jetbrains.kotlin.parcelize)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.google.playlistmaker"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.google.playlistmaker"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

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
        sourceCompatibility = JavaVersion.valueOf(libs.versions.java.get())
        targetCompatibility = JavaVersion.valueOf(libs.versions.java.get())
    }
    kotlinOptions {
        jvmTarget = JavaVersion.valueOf(libs.versions.java.get()).toString()
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Glide
    implementation(libs.com.github.bumptech.glide)
    //Retrofit2
    implementation(libs.com.squareup.retrofit2.retrofit)
    implementation(libs.com.squareup.retrofit2.converter.gson)
    implementation(libs.com.squareup.okhttp3.okhttp)
    implementation(libs.com.squareup.okhttp3.logging.interceptor)
    //Koin
    implementation(libs.io.insert.koin)
    //Viewpager2
    implementation(libs.androidx.viewpager2)
    //Navigation Graph
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    //Coroutines
    implementation(libs.org.jetbrains.coroutines.android)
    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

}