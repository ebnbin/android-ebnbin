package com.ebnbin.eb2.util

import android.content.Context
import android.graphics.Point
import android.view.Display
import android.view.WindowManager
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.requireSystemService

object WindowHelper {
    private val display: Display
        get() = EBApplication.instance.requireSystemService<WindowManager>().defaultDisplay

    val displayRotation: Int
        get() = display.rotation

    val displaySize: RotationSize
        get() {
            val display = display
            val outSize = Point()
            display.getSize(outSize)
            return RotationSize(outSize.x, outSize.y, display.rotation)
        }

    val displayRealSize: RotationSize
        get() {
            val display = display
            val outSize = Point()
            display.getRealSize(outSize)
            return RotationSize(outSize.x, outSize.y, display.rotation)
        }

    fun getDisplaySize(context: Context): RotationSize {
        val displayMetrics = context.resources.displayMetrics
        return RotationSize(displayMetrics.widthPixels, displayMetrics.heightPixels, displayRotation)
    }
}
