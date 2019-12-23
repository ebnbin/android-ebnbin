package com.ebnbin.eb

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.toTimeString(pattern: String = "yyyy-MM-dd HH:mm:ss:SSS", locale: Locale = Locale.getDefault()): String =
    SimpleDateFormat(pattern, locale).format(this)
