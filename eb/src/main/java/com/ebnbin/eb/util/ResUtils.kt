package com.ebnbin.eb.util

import android.content.res.Resources
import kotlin.math.roundToInt

val res: Resources
    get() = ebApp.resources

//*********************************************************************************************************************

val Float.dpToPx: Float
    get() = this * ResHelper.density

val Float.dpToPxRound: Int
    get() = dpToPx.roundToInt()

@Deprecated("使用 dpToPxRound", ReplaceWith(""))
val Float.dpToPxInt: Int
    get() = dpToPx.toInt()

val Float.spToPx: Float
    get() = this * ResHelper.scaledDensity

val Float.spToPxRound: Int
    get() = spToPx.roundToInt()

@Deprecated("使用 spToPxRound", ReplaceWith(""))
val Float.spToPxInt: Int
    get() = spToPx.toInt()
