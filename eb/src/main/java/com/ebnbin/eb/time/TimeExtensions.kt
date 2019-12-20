package com.ebnbin.eb.time

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDate(): Date {
    return Date(this)
}

fun Long.toTimeString(pattern: String): String {
    return toDate().toTimeString(pattern)
}

fun Date.toTimestamp(): Long {
    return time
}

fun Date.toTimeString(pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}
