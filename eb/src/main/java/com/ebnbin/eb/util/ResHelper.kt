package com.ebnbin.eb.util

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.getColorOrThrow

object ResHelper {
    val density: Float
        get() = res.displayMetrics.density

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
     * @param any 如果为 Int 则取字符串资源.
     */
    fun getString(any: Any?): String {
        return if (any is Int) {
            res.getString(any)
        } else {
            any.toString()
        }
    }
}
