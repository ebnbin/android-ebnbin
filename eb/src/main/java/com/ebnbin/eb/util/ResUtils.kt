package com.ebnbin.eb.util

import android.content.res.Resources
import kotlin.math.roundToInt

val res: Resources
    get() = ebApp.resources

//*********************************************************************************************************************

val Float.dpToPx: Float
    get() = this * ebApp.resources.displayMetrics.density

val Float.dpToPxRound: Int
    get() = dpToPx.roundToInt()

val Float.dpToPxInt: Int
    get() = dpToPx.toInt()
