package com.ebnbin.eb.util

import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.getColorOrThrow

object ResHelper {
    private val displayMetrics: DisplayMetrics
        get() = res.displayMetrics

    val density: Float
        get() = displayMetrics.density

    val scaledDensity: Float
        get() = displayMetrics.scaledDensity

    @ColorInt
    fun getColorAttr(context: Context, @AttrRes attrId: Int): Int {
        val attrs = intArrayOf(attrId)
        val typedArray = context.obtainStyledAttributes(attrs)
        val index = 0
        val color = typedArray.getColorOrThrow(index)
        typedArray.recycle()
        return color
    }

    /**
     * @param any 如果为 Int 则取字符串资源. 如果为 null 则返回 "null".
     */
    fun getString(any: Any?): String {
        return if (any is Int) {
            res.getString(any)
        } else {
            any.toString()
        }
    }
}
