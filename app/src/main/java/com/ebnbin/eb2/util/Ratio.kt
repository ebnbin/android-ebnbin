package com.ebnbin.eb2.util

/**
 * 宽高比. 需要自行确保宽高最大公约数为 1.
 *
 * @param width 必须大于 0.
 *
 * @param height 必须大于 0.
 */
open class Ratio(val width: Int, val height: Int) : Comparable<Ratio> {
    init {
        if (width <= 0 || height <= 0) throw RuntimeException()
    }

    val ratio: Float = width.toFloat() / height

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Ratio
        return width == other.width && height == other.height
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        return result
    }

    override fun compareTo(other: Ratio): Int {
        return compareValues(ratio, other.ratio)
    }

    override fun toString(): String {
        return "$width:$height"
    }

    companion object {
        /**
         * 1:1.
         */
        val SQUARE: Ratio = Ratio(1, 1)
    }
}
