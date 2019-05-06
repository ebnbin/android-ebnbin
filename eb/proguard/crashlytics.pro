# Crashlytics
# https://firebase.google.com/docs/crashlytics/get-deobfuscated-reports?platform=android

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
