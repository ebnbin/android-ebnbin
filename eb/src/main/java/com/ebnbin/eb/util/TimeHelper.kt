package com.ebnbin.eb.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeHelper {
    fun nano(): Long {
        return System.nanoTime()
    }

    fun long(): Long {
        return System.currentTimeMillis()
    }

    fun date(): Date {
        val long = long()
        return longToDate(long)
    }

    fun string(pattern: String): String {
        val date = date()
        return dateToString(date, pattern)
    }

    fun longToDate(long: Long): Date {
        return Date(long)
    }

    fun longToString(long: Long, pattern: String): String {
        val date = longToDate(long)
        return dateToString(date, pattern)
    }

    fun dateToLong(date: Date): Long {
        return date.time
    }

    fun dateToString(date: Date, pattern: String): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
    }

    fun expired(lastTimestamp: Long, expiration: Long): Boolean {
        return long() - lastTimestamp >= expiration
    }

    fun duration(startTimestamp: Long): Long {
        return long() - startTimestamp
    }
}
