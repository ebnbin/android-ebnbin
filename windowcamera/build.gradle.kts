plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.ebnbin.windowcamera"
        minSdkVersion(23)
        targetSdkVersion(29)
        versionCode = "0.6".split(".").let {
            1000000 * it[0].toInt() +
                    10000 * it[1].toInt() +
                    100 * it.getOrElse(2) { "0" }.toInt() +
                    it.getOrElse(3) { "0" }.toInt()
        }
        versionName = "0.6"

        ndk {
            abiFilters.add("armeabi-v7a")/*, 'x86'*/
        }
    }
    signingConfigs {
        register("release") {
            val signingPassword = rootProject.requireLocalProperty("signingPassword")
            storeFile = rootProject.file("gitignore/ebnbin.jks")
            storePassword = signingPassword
            keyAlias = "ebnbin"
            keyPassword = signingPassword
        }
    }
    buildTypes {
        named("release") {
            signingConfig = signingConfigs.getByName("release")
            val proguardFiles = project.file("proguard").listFiles() ?: emptyArray()
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), *proguardFiles)
            isMinifyEnabled = true
            isShrinkResources = true
        }
        named("debug") {
            signingConfig = signingConfigs.getByName("release")
        }
    }
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
//    applicationVariants.all { applicationVariant ->
//        applicationVariant.outputs.all {
//            outputFileName = "$applicationId" +
//                "-$versionName" +
//                "-${applicationVariant.flavorName}" +
//                "-${applicationVariant.buildType.name}.apk"
//        }
//    }

    sourceSets {
        configureEach {
            val srcDirs = project.file("src/$name")
                .listFiles { file -> file.isDirectory && file.name.startsWith("res-") }
                ?: emptyArray()
            res.srcDirs(*srcDirs)
        }
    }
}

dependencies {
    implementation(project(":ebapp"))

    api("com.github.chrisbanes:PhotoView:2.3.0") // https://github.com/chrisbanes/PhotoView
    api("com.google.android.exoplayer:exoplayer:2.11.1") // https://github.com/google/ExoPlayer




    api("io.reactivex.rxjava2:rxjava:2.2.12") // https://github.com/ReactiveX/RxJava
    api("io.reactivex.rxjava2:rxandroid:2.1.1") // https://github.com/ReactiveX/RxAndroid
    api("com.squareup.retrofit2:adapter-rxjava2:2.7.0")
    api("com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.46") // https://github.com/CymChad/BaseRecyclerViewAdapterHelper
}
