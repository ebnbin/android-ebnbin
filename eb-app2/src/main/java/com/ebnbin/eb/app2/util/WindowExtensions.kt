package com.ebnbin.eb.app2.util

import android.content.Context
import android.graphics.Point
import android.view.Surface
import android.view.WindowManager
import com.ebnbin.eb.requireSystemService

fun Context.getDisplayRealSize(): Triple<Int, Int, Int> {
    val display = requireSystemService<WindowManager>().defaultDisplay
    val outSize = Point().also {
        display.getRealSize(it)
    }
    return Triple(outSize.x, outSize.y, display.rotation)
}

fun Context.getDisplayRealSize0(): Pair<Int, Int> {
    val displayRealSize = getDisplayRealSize()
    return if (displayRealSize.third == Surface.ROTATION_90 || displayRealSize.third == Surface.ROTATION_270) {
        Pair(displayRealSize.second, displayRealSize.first)
    } else {
        Pair(displayRealSize.first, displayRealSize.second)
    }
}
