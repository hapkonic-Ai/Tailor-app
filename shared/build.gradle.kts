import java.util.Properties
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val localProps = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.let { load(it) }
}

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)

            // DI
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // ViewModel
            implementation(libs.lifecycle.viewmodel)

            // Kotlinx
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kotlinx.serialization)
            implementation(libs.kotlinx.datetime)

            // Network
            implementation(libs.ktor.core)

            // Local DB
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)

            // Firebase — requires google-services.json (Android) + GoogleService-Info.plist (iOS)
            implementation(libs.firebase.firestore)
            implementation(libs.firebase.storage)
            // firebase-auth removed — using local credential auth (LocalAuthService)
        }

        androidMain.dependencies {
            implementation(libs.ktor.okhttp)
            implementation(libs.sqldelight.android.driver)
            implementation(libs.koin.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.darwin)
            implementation(libs.sqldelight.native.driver)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
        }
    }
}

android {
    namespace = "com.hapkonic.tailorapp"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
        // Dev default: local storage. Override with USE_FIREBASE_STORAGE=true in local.properties.
        buildConfigField(
            "boolean",
            "USE_FIREBASE_STORAGE",
            localProps.getProperty("USE_FIREBASE_STORAGE", "false")
        )
    }
    buildTypes {
        release {
            // Production builds always use Firebase Storage.
            buildConfigField("boolean", "USE_FIREBASE_STORAGE", "true")
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            // Schema .sq files go in: shared/src/commonMain/sqldelight/com/hapkonic/tailorapp/db/
            packageName.set("com.hapkonic.tailorapp.db")
        }
    }
}
