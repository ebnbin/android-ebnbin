package com.ebnbin.eb.util

import android.view.Surface
import kotlin.math.roundToInt

open class RotationSize(val width: Int, val height: Int, val rotation: Int): Comparable<RotationSize> {
    init {
        if (width <= 0 ||
            height <= 0 ||
            rotation != Surface.ROTATION_0 &&
            rotation != Surface.ROTATION_90 &&
            rotation != Surface.ROTATION_180 &&
            rotation != Surface.ROTATION_270) {
            throw RuntimeException()
        }
    }

    fun width(rotation: Int): Int {
        return when (rotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 -> {
                if (this.rotation == Surface.ROTATION_0 || this.rotation == Surface.ROTATION_180) width else height
            }
            Surface.ROTATION_90, Surface.ROTATION_270 -> {
                if (this.rotation == Surface.ROTATION_90 || this.rotation == Surface.ROTATION_270) width else height
            }
            else -> throw RuntimeException()
        }
    }

    fun height(rotation: Int): Int {
        return when (rotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 -> {
                if (this.rotation == Surface.ROTATION_0 || this.rotation == Surface.ROTATION_180) height else width
            }
            Surface.ROTATION_90, Surface.ROTATION_270 -> {
                if (this.rotation == Surface.ROTATION_90 || this.rotation == Surface.ROTATION_270) height else width
            }
            else -> throw RuntimeException()
        }
    }

    //*****************************************************************************************************************

    val width0: Int = width(Surface.ROTATION_0)

    val height0: Int = height(Surface.ROTATION_0)

    val area: Int = width0 * height0

    private val gcd: Int = width0 gcd height0

    /**
     * 宽高比以 rotation 为 0 时的宽高为准.
     */
    val ratio: Ratio = Ratio(width0 / gcd, height0 / gcd)

    fun isWidthHeightGreaterOrEquals(other: RotationSize): Boolean {
        return width0 >= other.width0 && height0 >= other.height0
    }

    fun crop(ratio: Ratio, percent: Int = 100): RotationSize {
        if (percent <= 0 || percent > 100) throw RuntimeException()
        val newWidth0: Int
        val newHeight0: Int
        if (this.ratio < ratio) {
            newWidth0 = (width0 * percent / 100f).roundToInt()
            newHeight0 = (newWidth0 / ratio.ratio).roundToInt()
        } else {
            newHeight0 = (height0 * percent / 100f).roundToInt()
            newWidth0 = (newHeight0 * ratio.ratio).roundToInt()
        }
        val newWidth: Int
        val newHeight: Int
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            newWidth = newWidth0
            newHeight = newHeight0
        } else {
            newWidth = newHeight0
            newHeight = newWidth0
        }
        return RotationSize(newWidth, newHeight, rotation)
    }

    //*****************************************************************************************************************

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as RotationSize
        return width0 == other.width0 && height0 == other.height0
    }

    override fun hashCode(): Int {
        var result = width0
        result = 31 * result + height0
        return result
    }

    override fun compareTo(other: RotationSize): Int {
        return compareValuesBy(this, other, RotationSize::area, RotationSize::width0)
    }
}
