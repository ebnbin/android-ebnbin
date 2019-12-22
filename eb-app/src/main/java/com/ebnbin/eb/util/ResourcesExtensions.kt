package com.ebnbin.eb.util

import android.content.Context
import java.util.Locale

val Context.locales: List<Locale>
    get() = if (sdk24N()) {
        resources.configuration.locales.let { locales ->
            (0 until locales.size()).map { locales.get(it) }
        }
    } else {
        @Suppress("DEPRECATION")
        listOf(resources.configuration.locale)
    }
