package com.ebnbin.eb.util

import android.content.Context
import androidx.core.content.getSystemService

inline fun <reified T : Any> Context.requireSystemService(): T {
    return getSystemService()!!
}
