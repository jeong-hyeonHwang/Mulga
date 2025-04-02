import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
//    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
    kotlin("plugin.serialization") version "2.1.0"
}

android {
    namespace = "com.example.mulga"
    compileSdk = 35

    val localProperties = Properties()
    localProperties.load(FileInputStream(rootProject.file("local.properties")))

    defaultConfig {
        applicationId = "com.example.mulga"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val props = gradleLocalProperties(
            rootDir,
            providers
        )

        val rabbitHost = props.getProperty("rabbitmq.host") ?: throw GradleException("❌ rabbitmq.host 없음")
        val rabbitPort = props.getProperty("rabbitmq.port")?.toIntOrNull() ?: throw GradleException("❌ rabbitmq.port 없음")
        val rabbitUser = props.getProperty("rabbitmq.username") ?: throw GradleException("❌ rabbitmq.username 없음")
        val rabbitPass = props.getProperty("rabbitmq.password") ?: throw GradleException("❌ rabbitmq.password 없음")

        buildConfigField("String", "RABBITMQ_HOST", "\"$rabbitHost\"")
        buildConfigField("int", "RABBITMQ_PORT", rabbitPort.toString())
        buildConfigField("String", "RABBITMQ_USERNAME", "\"$rabbitUser\"")
        buildConfigField("String", "RABBITMQ_PASSWORD", "\"$rabbitPass\"")
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.runtime.livedata)
    val room_version = "2.6.1"

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("io.insert-koin:koin-core:4.0.2")
    implementation("io.insert-koin:koin-android:4.0.2")
    implementation("io.insert-koin:koin-androidx-compose:4.0.2")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("com.rabbitmq:amqp-client:5.21.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("androidx.lifecycle:lifecycle-process:2.6.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    kapt("androidx.room:room-compiler:$room_version")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("io.github.thechance101:chart:Beta-0.0.5")
}