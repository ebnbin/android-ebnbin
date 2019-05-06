# eb

-keep interface com.ebnbin.eb.util.Keep { *; }
-keep class * implements com.ebnbin.eb.util.Keep { *; }

-keepnames class com.ebnbin.eb.app.EBActivity
-keepnames class * extends com.ebnbin.eb.app.EBActivity
-keepnames class com.ebnbin.eb.app.EBFragment
-keepnames class * extends com.ebnbin.eb.app.EBFragment
