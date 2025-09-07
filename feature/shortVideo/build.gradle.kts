plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlinx-serialization")
}

android {
    namespace = "com.nlhd.shortVideo"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(project(":domain"))
    implementation(project(":core"))
    implementation(project(":keystore"))

    //Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    //ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.9.3")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.3")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //Koin
    implementation("io.insert-koin:koin-android:4.1.1")
    implementation("io.insert-koin:koin-androidx-compose:4.1.1")

    //Image
    implementation("io.coil-kt:coil-compose:2.7.0")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.7")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.9.3")

    //Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    //Paging
    implementation("androidx.paging:paging-runtime:3.3.6")
    implementation("androidx.paging:paging-compose:3.3.6")

    //Video
    implementation("androidx.media3:media3-exoplayer:1.8.0")
    implementation("androidx.media3:media3-exoplayer-dash:1.5.0")
    implementation("androidx.media3:media3-ui:1.8.0")
    implementation("androidx.media:media:1.7.0")
    implementation("androidx.media3:media3-datasource-okhttp:1.8.0") // Optional for OkHttpDataSource
    implementation("androidx.media3:media3-datasource:1.8.0")

    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")
}