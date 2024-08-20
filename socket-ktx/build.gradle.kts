plugins {
    alias(libs.plugins.xbot.android.library)
}
android {
    namespace = "com.xbot.socket"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.socket.io.client)

    // Kotlin dependencies
    implementation(libs.kotlinx.coroutines.core)

    // Testing dependencies
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
}