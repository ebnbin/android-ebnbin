plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(29)
        val proguardFiles = project.file("proguard").listFiles() ?: emptyArray()
        consumerProguardFiles(*proguardFiles)

        buildConfigField("String", "GITHUB_API_TOKEN", "\"${rootProject.requireLocalProperty("githubApiToken")}\"")
    }
    sourceSets {
        configureEach {
            val srcDirs = project.file("src/$name")
                .listFiles { file -> file.isDirectory && file.name.startsWith("res-") }
                ?: emptyArray()
            res.srcDirs(*srcDirs)
        }
    }
//    buildTypes {
//        release {
////            manifestPlaceholders = [firebaseCrashlyticsCollectionEnabled: true]
//        }
//        debug {
////            manifestPlaceholders = [firebaseCrashlyticsCollectionEnabled: false]
//        }
//    }
    flavorDimensions("flavor")
    productFlavors {
        register("github") {
            dimension("flavor")
        }
        register("google") {
            dimension("flavor")
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
        viewBinding = true
    }
    resourcePrefix("ebapp")
}

dependencies {
    api(project(":eb"))
    api("com.google.firebase:firebase-crashlytics:17.4.1")
    api("com.google.firebase:firebase-analytics:18.0.3") // https://firebase.google.com/docs/android/setup
//    api 'com.crashlytics.sdk.android:crashlytics:2.10.1' // https://firebase.google.com/docs/crashlytics/get-started?platform=android
}
