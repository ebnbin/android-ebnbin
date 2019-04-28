package com.ebnbin.eb.util

import android.content.Context
import android.content.res.Resources
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.res.getColorOrThrow

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

    @ColorInt
    fun getColor(context: Context, @ColorRes colorId: Int, theme: Resources.Theme? = null): Int {
        return context.resources.getColor(colorId, theme)
    }
}
