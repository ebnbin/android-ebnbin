package com.ebnbin.eb.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeHelper {
    fun long(): Long {
        return System.currentTimeMillis()
    }

    fun date(): Date {
        return Date(long())
    }

    fun string(pattern: String): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(date())
    }
}
