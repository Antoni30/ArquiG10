plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // ❌ Elimina esta línea:
    // alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ec.edu.monster.climov_dotnet_soap"
    compileSdk = 36

    defaultConfig {
        applicationId = "ec.edu.monster.climov_dotnet_soap"
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

    // ✅ Desactiva Compose y activa ViewBinding (opcional pero útil)
    buildFeatures {
        compose = false
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

    // ✅ Coroutines para llamadas SOAP asíncronas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ✅ Opcional: solo si usas viewBinding
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ✅ Pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}