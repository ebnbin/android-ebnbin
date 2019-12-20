package com.ebnbin.eb.widget

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

private var toast: Toast? = null

fun Context.toast(@StringRes resId: Int, durationLong: Boolean = false) {
    toast?.let {
        toast = null
        it.cancel()
    }
    toast = Toast.makeText(this, resId, if (durationLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).also {
        it.show()
    }
}

fun Context.toast(text: CharSequence, durationLong: Boolean = false) {
    toast?.let {
        toast = null
        it.cancel()
    }
    toast = Toast.makeText(this, text, if (durationLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).also {
        it.show()
    }
}
