plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "edu.umg.controlagua"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "edu.umg.controlagua"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // ✅ AQUÍ ESTÁ EL VIEW BINDING, EN EL LUGAR CORRECTO
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // ✅ AQUÍ ESTÁN TUS DEPENDENCIAS
    implementation("umg.edu.gt.progra2:agua-core:1.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}