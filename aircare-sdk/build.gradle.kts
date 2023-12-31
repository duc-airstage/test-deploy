
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}


android {
    namespace = "com.example.aircare_sdk"
    compileSdk = 33

    defaultConfig {
        minSdk = 26

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }


    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }





}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.health.connect:connect-client:1.1.0-alpha06")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
}


publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.duc-airstage"
            artifactId = "test-deploy"
            version = "0.0.1"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}




