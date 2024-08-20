plugins {
    alias(libs.plugins.xbot.android.library)
    alias(libs.plugins.xbot.android.hilt)
}
android {
    namespace = "com.xbot.data"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // Project-level dependencies
    implementation(project(":core:common"))
    implementation(project(":domain"))
    implementation(project(":socket-ktx"))

    implementation(libs.socket.io.client)

    // AndroidX dependencies
    implementation(libs.androidx.dataStore)

    // Kotlin dependencies
    implementation(libs.kotlinx.datetime)

    // Testing dependencies
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
}