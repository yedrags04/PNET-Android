plugins {
    alias(libs.plugins.android.application)
    // AGP 9+: Kotlin va integrado en el plugin de Android; no añadir org.jetbrains.kotlin.android.
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.heistcorp.heistcraft"
    compileSdk = 36

    sourceSets {
        getByName("main").java.srcDir("src/main/kotlin")
        getByName("test").java.srcDir("src/test/kotlin")
        getByName("androidTest").java.srcDir("src/androidTest/kotlin")
    }

    defaultConfig {
        applicationId = "com.heistcorp.heistcraft"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Emulador Android → máquina host: 10.0.2.2. En dispositivo físico, cambia a http://TU_IP_LAN:8080/
        // API propia del repo: PNET-Android/api/animal-api-mongo (npm start, PORT en .env, por defecto 3000)
        buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:3000/\"")
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
        // Actualizado a Java 17 para compatibilidad con las últimas versiones
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    // Retrofit y red
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)
    implementation(libs.okhttp)

    // Imágenes (Coil)
    implementation(libs.coil.compose)

    // Corrutinas
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}