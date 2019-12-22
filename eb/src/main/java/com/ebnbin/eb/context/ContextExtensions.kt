package com.ebnbin.eb.context

import android.content.Context
import androidx.core.content.getSystemService

inline fun <reified T : Any> Context.requireSystemService(): T = getSystemService() ?: throw NullPointerException()
