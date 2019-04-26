package com.ebnbin.eb.crash

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class CrashRuntimeException :
    RuntimeException(SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault()).format(Date()))
