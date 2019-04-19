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

    //*****************************************************************************************************************

    /**
     * 指定旋转方向的宽.
     */
    val widths: Map<Int, Int> = LinkedHashMap<Int, Int>().apply {
        arrayOf(
            Surface.ROTATION_0,
            Surface.ROTATION_90,
            Surface.ROTATION_180,
            Surface.ROTATION_270
        ).forEach {
            val width = if (it == Surface.ROTATION_0 || it == Surface.ROTATION_180) {
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) width else height
            } else {
                if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) width else height
            }
            put(it, width)
        }
    }

    /**
     * 指定旋转方向的高.
     */
    val heights: Map<Int, Int> = LinkedHashMap<Int, Int>().apply {
        arrayOf(
            Surface.ROTATION_0,
            Surface.ROTATION_90,
            Surface.ROTATION_180,
            Surface.ROTATION_270
        ).forEach {
            val height = if (it == Surface.ROTATION_0 || it == Surface.ROTATION_180) {
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) height else width
            } else {
                if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) height else width
            }
            put(it, height)
        }
    }

    private val width0: Int = widths.getValue(Surface.ROTATION_0)

    //*****************************************************************************************************************

    /**
     * 面积.
     */
    val area: Int = width * height

    /**
     * 宽高最大公约数.
     */
    private val gcd: Int = width gcd height

    /**
     * 宽高比以旋转方向为 [Surface.ROTATION_0] 时的宽高为准.
     */
    val ratio: Ratio = Ratio(widths.getValue(Surface.ROTATION_0) / gcd, heights.getValue(Surface.ROTATION_0) / gcd)

    //*****************************************************************************************************************

    /**
     * 宽高都大等于指定对象.
     */
    fun isWidthHeightGreaterOrEquals(other: RotationSize): Boolean {
        return widths.getValue(Surface.ROTATION_0) >= other.widths.getValue(Surface.ROTATION_0) &&
                heights.getValue(Surface.ROTATION_0) >= other.heights.getValue(Surface.ROTATION_0)
    }

    /**
     * 按照指定宽高比裁剪尺寸.
     *
     * @param ratio 指定宽高比.
     *
     * @param scale 缩放. 必须大于 0f. 值为 1f 时不缩放.
     *
     * @return 裁剪后的新的尺寸. 旋转方向保持不变.
     */
    fun crop(ratio: Ratio, scale: Float = 1f): RotationSize {
        if (scale <= 0f) throw RuntimeException()
        var newWidth0: Int
        var newHeight0: Int
        if (this.ratio < ratio) {
            newWidth0 = (widths.getValue(Surface.ROTATION_0) * scale).roundToInt()
            newHeight0 = (newWidth0 / ratio.ratio).roundToInt()
        } else {
            newHeight0 = (heights.getValue(Surface.ROTATION_0) * scale).roundToInt()
            newWidth0 = (newHeight0 * ratio.ratio).roundToInt()
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
        return widths.getValue(Surface.ROTATION_0) == other.widths.getValue(Surface.ROTATION_0) &&
                heights.getValue(Surface.ROTATION_0) == other.heights.getValue(Surface.ROTATION_0)
    }

    override fun hashCode(): Int {
        var result = widths.getValue(Surface.ROTATION_0)
        result = 31 * result + heights.getValue(Surface.ROTATION_0)
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
