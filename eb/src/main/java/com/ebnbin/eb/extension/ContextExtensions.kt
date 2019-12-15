package com.ebnbin.eb.extension

import android.content.Context

fun Context.dpToPx(dp: Float): Float {
    return resources.displayMetrics.density * dp
}
