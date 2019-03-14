package com.ebnbin.eb.util

import android.content.Context
import android.widget.Toast
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

//*********************************************************************************************************************

/**
 * @param any 如果为 Int 则取字符串资源.
 */
fun toast(context: Context, any: Any?, long: Boolean = false) {
    val duration = if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    if (any is Int) {
        Toast.makeText(context, any, duration).show()
    } else {
        Toast.makeText(context, any.toString(), duration).show()
    }
}
