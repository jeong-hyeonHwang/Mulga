import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
//    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
    kotlin("plugin.serialization") version "2.1.0"
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp") version "2.0.21-1.0.28"
}

val keyPropertiesFile = rootProject.file("./app/key.properties")
val properties = Properties()
properties.load(FileInputStream(keyPropertiesFile))

android {
    namespace = "com.ilm.mulga"
    compileSdk = 35

    val localProperties = Properties()
    localProperties.load(FileInputStream(rootProject.file("local.properties")))

    defaultConfig {
        applicationId = "com.ilm.mulga"
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

        val backendHost = props.getProperty("backend.host") ?: throw GradleException("❌ backend.host 없음")
        val testAccessToken = props.getProperty("testAccessToken") ?: throw GradleException("❌ testAccessToken 없음")
        buildConfigField("String", "BACKEND_HOST", "\"$backendHost\"")
        buildConfigField("String", "TEST_ACCESS_TOKEN", "\"$testAccessToken\"")
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

    signingConfigs {
        getByName("debug") {
            storeFile = properties["storeFile"]?.toString()?.let { file(it) }
            storePassword = properties["storePassword"]?.toString()
            keyAlias = properties["keyAlias"]?.toString()
            keyPassword = properties["keyPassword"]?.toString()
        }
        create("release") {
            storeFile = properties["storeFile"]?.toString()?.let { file(it) }
            storePassword = properties["storePassword"]?.toString()
            keyAlias = properties["keyAlias"]?.toString()
            keyPassword = properties["keyPassword"]?.toString()
        }
    }
}

dependencies {
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.googleid)
    val room_version = "2.6.1"

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("io.insert-koin:koin-core:4.0.2")
    implementation("io.insert-koin:koin-android:4.0.2")
    implementation("io.insert-koin:koin-androidx-compose:4.0.2")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("com.rabbitmq:amqp-client:5.21.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation ("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("androidx.lifecycle:lifecycle-process:2.6.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    ksp("androidx.room:room-compiler:$room_version")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("io.github.thechance101:chart:Beta-0.0.5")
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:21.3.0")
}