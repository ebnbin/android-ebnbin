package com.ebnbin.eb2.util

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.getColorOrThrow
import com.ebnbin.eb.EBApp

object ResHelper {
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
            EBApp.instance.resources.getString(any)
        } else {
            any.toString()
        }
    }
}
