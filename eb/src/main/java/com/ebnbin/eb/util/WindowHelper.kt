package com.ebnbin.eb.util

import android.graphics.Point
import android.view.Display

/**
 * Window 帮助类.
 */
object WindowHelper {
    private val display: Display
        get() = SystemServices.windowManager.defaultDisplay

    /**
     * 不要频繁调用.
     */
    val displayRotation: Int
        get() = display.rotation

    /**
     * 不要频繁调用.
     */
    val displaySize: RotationSize
        get() {
            val display = display
            val outSize = Point()
            display.getSize(outSize)
            return RotationSize(outSize.x, outSize.y, display.rotation)
        }

    /**
     * 不要频繁调用.
     */
    val displayRealSize: RotationSize
        get() {
            val display = display
            val outSize = Point()
            display.getRealSize(outSize)
            return RotationSize(outSize.x, outSize.y, display.rotation)
        }
}
