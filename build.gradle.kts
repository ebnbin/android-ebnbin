buildscript { 
    repositories {
        google()
        jcenter()
//        maven { url 'https://maven.fabric.io/public' }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0") // https://github.com/JetBrains/kotlin
        classpath("com.google.gms:google-services:4.3.5") // https://firebase.google.com/docs/android/setup
//        classpath 'io.fabric.tools:gradle:1.31.2' // https://firebase.google.com/docs/crashlytics/get-started?platform=android
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.5.2")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
