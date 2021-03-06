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
        buildConfigField("long", "BUILD_TIMESTAMP", "${System.currentTimeMillis()}L")
    }
    sourceSets {
        configureEach {
            val srcDirs = project.file("src/$name")
                .listFiles { file -> file.isDirectory && file.name.startsWith("res-") }
                ?: emptyArray()
            res.srcDirs(*srcDirs)
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
    resourcePrefix("eb")
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.0") // https://github.com/JetBrains/kotlin
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3") // https://github.com/Kotlin/kotlinx.coroutines
    api("androidx.core:core-ktx:1.2.0-rc01")  // https://maven.google.com/web/index.html https://developer.android.com/jetpack/androidx/versions
    api("androidx.appcompat:appcompat:1.2.0-alpha01")
    api("androidx.activity:activity-ktx:1.1.0-rc03")
    api("androidx.fragment:fragment-ktx:1.2.0-rc04")
    api("androidx.annotation:annotation:1.1.0")
    api("androidx.collection:collection-ktx:1.1.0")
    api("androidx.preference:preference-ktx:1.1.0")
    api("androidx.recyclerview:recyclerview:1.1.0")
    api("androidx.viewpager:viewpager:1.0.0")
    api("androidx.cardview:cardview:1.0.0")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0-rc03")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0-rc03")
    api("com.google.android.material:material:1.2.0-alpha03") // https://material.io/develop/android/docs/getting-started/
    api("com.google.code.gson:gson:2.8.6") // https://github.com/google/gson
    api("cat.ereza:customactivityoncrash:2.2.0") // https://github.com/Ereza/CustomActivityOnCrash
    debugApi("com.squareup.leakcanary:leakcanary-android:2.1") // https://github.com/square/leakcanary
    api("com.jeremyliao:live-event-bus-x:1.5.7") // https://github.com/JeremyLiao/LiveEventBus
    api("com.github.bumptech.glide:glide:4.10.0") // https://github.com/bumptech/glide
    kapt("com.github.bumptech.glide:compiler:4.10.0")
    api("com.squareup.okhttp3:okhttp:4.2.2") // https://github.com/square/okhttp
    api("com.squareup.retrofit2:retrofit:2.7.0") // https://github.com/square/retrofit
    api("com.squareup.retrofit2:converter-gson:2.7.0")
}
