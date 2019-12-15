package com.ebnbin.eb.extension

import android.content.Context
import kotlin.math.roundToInt

fun Context.dpToPx(dp: Float): Float {
    return dp * resources.displayMetrics.density
}

fun Context.dpToPxRound(dp: Float): Int {
    return dpToPx(dp).roundToInt()
}

fun Context.spToPx(sp: Float): Float {
    return sp * resources.displayMetrics.scaledDensity
}

fun Context.spToPxRound(sp: Float): Int {
    return spToPx(sp).roundToInt()
}

fun Context.pxToDp(px: Float): Float {
    return px / resources.displayMetrics.density
}

fun Context.pxToDp(px: Int): Float {
    return pxToDp(px.toFloat())
}

fun Context.pxToSp(px: Float): Float {
    return px / resources.displayMetrics.scaledDensity
}

fun Context.pxToSp(px: Int): Float {
    return pxToSp(px.toFloat())
}
