package com.ebnbin.eb.extension

import android.content.Context
import kotlin.math.roundToInt

fun Context.dpToPx(dp: Float): Float {
    return dp * resources.displayMetrics.density
}

/**
 * 四舍五入取整.
 */
fun Context.dpToPxRound(dp: Float): Int {
    return dpToPx(dp).roundToInt()
}

/**
 * 向下取整.
 */
@Deprecated("使用 dpToPxRound 代替.", ReplaceWith(""))
fun Context.dpToPxInt(dp: Float): Int {
    return dpToPx(dp).toInt()
}

fun Context.spToPx(sp: Float): Float {
    return sp * resources.displayMetrics.scaledDensity
}

/**
 * 四舍五入取整.
 */
fun Context.spToPxRound(sp: Float): Int {
    return spToPx(sp).roundToInt()
}

/**
 * 向下取整.
 */
@Deprecated("使用 spToPxRound 代替.", ReplaceWith(""))
fun Context.spToPxInt(sp: Float): Int {
    return spToPx(sp).toInt()
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
