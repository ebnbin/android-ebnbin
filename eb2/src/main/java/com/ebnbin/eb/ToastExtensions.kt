package com.ebnbin.eb

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

private var toast: Toast? = null

private fun cancelToast() {
    toast?.let {
        toast = null
        it.cancel()
    }
}

/**
 * 展示时会取消上一条 toast.
 */
fun Context.toast(@StringRes resId: Int, durationLong: Boolean = false) {
    cancelToast()
    toast = Toast.makeText(
        // 使用 application context 避免 Activity 泄漏.
        this.applicationContext,
        resId,
        if (durationLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    ).also {
        it.show()
    }
}

fun Context.toast(text: CharSequence, durationLong: Boolean = false) {
    cancelToast()
    toast = Toast.makeText(
        this.applicationContext,
        text,
        if (durationLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    ).also {
        it.show()
    }
}
