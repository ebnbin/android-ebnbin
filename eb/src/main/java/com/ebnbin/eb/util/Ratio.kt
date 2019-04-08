package com.ebnbin.eb.util

/**
 * 宽高比.
 */
class Ratio(val width: Int, val height: Int) : Comparable<Ratio> {
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

    companion object {
        val SQUARE: Ratio = Ratio(1, 1)
    }
}
