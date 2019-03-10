package com.ebnbin.eb.util

import com.ebnbin.eb.app.EBApplication
import kotlin.math.roundToInt

/**
 * EBApplication 单例.
 */
val ebApp: EBApplication
    get() = EBApplication.instance

//*********************************************************************************************************************

val Float.dpToPx: Float
    get() = this * ebApp.resources.displayMetrics.density

val Float.dpToPxRound: Int
    get() = dpToPx.roundToInt()

val Float.dpToPxInt: Int
    get() = dpToPx.toInt()
