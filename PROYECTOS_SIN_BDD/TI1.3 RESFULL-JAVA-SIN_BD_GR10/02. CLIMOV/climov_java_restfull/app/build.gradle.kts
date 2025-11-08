plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // ❌ Elimina esta línea si usas vistas clásicas:
    // alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ec.edu.monster.climov_java_restfull"
    compileSdk = 36

    defaultConfig {
        applicationId = "ec.edu.monster.climov_java_restfull"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    // ✅ Habilita las características de vistas clásicas
    buildFeatures {
        // ❌ Desactiva Compose
        compose = false
        // ✅ Asegúrate de tener viewBinding (opcional pero útil)
        viewBinding = true
    }
}

dependencies {
    // ✅ Librerías esenciales para vistas clásicas
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.cardview:cardview:1.0.0")

    // ✅ Retrofit y Coroutines para REST
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.activity:activity-ktx:1.9.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ✅ Opcional: solo si usas viewBinding
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ✅ Pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}