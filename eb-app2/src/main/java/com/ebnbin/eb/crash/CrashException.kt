package com.ebnbin.eb.crash

import java.text.SimpleDateFormat
import java.util.Locale

internal class CrashException : RuntimeException(
    SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault()).format(System.currentTimeMillis())
)
