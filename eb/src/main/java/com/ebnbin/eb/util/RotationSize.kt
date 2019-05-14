package com.ebnbin.eb.util

import android.view.Surface
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * 带旋转方向的 size 工具类. 用于屏幕大小, 相机分辨率等尺寸.
 *
 * @param width 必须大于 0.
 *
 * @param height 必须大于 0.
 *
 * @param rotation 必须为 [Surface.ROTATION_0], [Surface.ROTATION_90], [Surface.ROTATION_180], [Surface.ROTATION_270]
 * 之一.
 */
open class RotationSize(val width: Int, val height: Int, val rotation: Int): Comparable<RotationSize>, EBModel {
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

    //*****************************************************************************************************************

    /**
     * 指定旋转方向的宽.
     */
    @Transient
    val widths: IntArray = arrayOf(
        Surface.ROTATION_0,
        Surface.ROTATION_90,
        Surface.ROTATION_180,
        Surface.ROTATION_270
    ).map {
        if (it == Surface.ROTATION_0 || it == Surface.ROTATION_180) {
            if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) width else height
        } else {
            if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) width else height
        }
    }.toIntArray()

    /**
     * 指定旋转方向的高.
     */
    @Transient
    val heights: IntArray = arrayOf(
        Surface.ROTATION_0,
        Surface.ROTATION_90,
        Surface.ROTATION_180,
        Surface.ROTATION_270
    ).map {
        if (it == Surface.ROTATION_0 || it == Surface.ROTATION_180) {
            if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) height else width
        } else {
            if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) height else width
        }
    }.toIntArray()

    @Transient
    val width0: Int = widths[Surface.ROTATION_0]

    @Transient
    val height0: Int = heights[Surface.ROTATION_0]

    //*****************************************************************************************************************

    /**
     * 面积.
     */
    @Transient
    val area: Int = width * height

    /**
     * 宽高最大公约数.
     */
    @Transient
    private val gcd: Int = width gcd height

    @Transient
    val ratio: Ratio = Ratio(width / gcd, height / gcd)

    @Transient
    val ratio0: Ratio = Ratio(width0 / gcd, height0 / gcd)

    //*****************************************************************************************************************

    /**
     * 宽高都大等于指定对象.
     */
    fun isWidthHeightGreaterOrEquals(other: RotationSize): Boolean {
        return width0 >= other.width0 && height0 >= other.height0
    }

    /**
     * 宽高都小等于指定对象.
     */
    fun isWidthHeightLessOrEquals(other: RotationSize): Boolean {
        return width0 <= other.width0 && height0 <= other.height0
    }

    /**
     * 按照指定宽高比裁剪尺寸.
     *
     * @param ratio0 指定宽高比.
     *
     * @param scale 缩放. 必须大于 0f. 值为 1f 时不缩放.
     *
     * @return 裁剪后的新的尺寸. 旋转方向保持不变.
     */
    fun crop(ratio0: Ratio, scale: Float = 1f): RotationSize {
        if (scale <= 0f) throw RuntimeException()
        var newWidth0: Int
        var newHeight0: Int
        if (this.ratio0 < ratio0) {
            newWidth0 = (width0 * scale).roundToInt()
            newHeight0 = (newWidth0 / ratio0.ratio).roundToInt()
        } else {
            newHeight0 = (height0 * scale).roundToInt()
            newWidth0 = (newHeight0 * ratio0.ratio).roundToInt()
        }
        // 宽高不能为 0.
        newWidth0 = max(1, newWidth0)
        newHeight0 = max(1, newHeight0)
        return if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            RotationSize(newWidth0, newHeight0, rotation)
        } else {
            RotationSize(newHeight0, newWidth0, rotation)
        }
    }

    //*****************************************************************************************************************

    /**
     * 当旋转方向相同时, 宽高相同视为相等.
     */
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

    /**
     * 面积优先, 旋转方向为 [Surface.ROTATION_0] 时的宽其次.
     */
    override fun compareTo(other: RotationSize): Int {
        return compareValuesBy(this, other, RotationSize::area, RotationSize::width0)
    }

    override fun toString(): String {
        return "${width}x$height"
    }
}
