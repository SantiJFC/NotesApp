plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.notesapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.notesapp"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.2")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")
    implementation("com.google.firebase:firebase-firestore-ktx:24.8.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.09.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")

    //Bibliotecas de datos de Material 2
    implementation ("androidx.compose.material:material:1.5.1")

    // Add the dependency for the Firebase SDK for Google Analytics:
    implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation("com.google.firebase:firebase-analytics:21.3.0")

    //Acceso con las cuentas de google
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.firebase:firebase-auth-ktx:22.1.2")
    implementation ("com.google.firebase:firebase-auth:22.1.2")

    //Librería para el almacenamientos de los datos (fotos incluidas). Si no funciona se deshabilita
    implementation ("com.google.firebase:firebase-storage-ktx:20.2.1")

    //Splash API:
    implementation ("androidx.core:core-splashscreen:1.0.1")

    // Librería de navigation compose:
    implementation("androidx.navigation:navigation-compose:2.7.2")

    // Iconos compose:
    implementation ("androidx.compose.material:material-icons-extended:1.5.1")

    //Banner de anuncios para el panel principal. Se suprime cuando se compra la aplicación
    implementation ("com.google.android.gms:play-services-ads:22.4.0")

    // Import the BoM for the Firebase platform
    implementation (platform("com.google.firebase:firebase-bom:32.3.1"))

    // Declare the dependencies for the desired Firebase products without specifying versions
    // For example, declare the dependencies for Firebase Authentication and Cloud Firestore
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-firestore")

}