package com.ebnbin.eb.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ebnbin.eb.activity.EBFragmentActivity

fun Context.dpToPx(dp: Float): Float {
    return resources.displayMetrics.density * dp
}

internal fun Context.createStartActivityIntent(intent: Intent?, activityClass: Class<out Activity>?): Intent {
    val result = if (intent == null) Intent() else Intent(intent)
    activityClass?.let { result.setClass(this, it) }
    return result
}

internal fun Context.createStartFragmentIntent(
    intent: Intent?,
    fragmentClass: Class<out Fragment>?,
    fragmentArguments: Bundle?
): Intent {
    val result = if (intent == null) Intent() else Intent(intent)
    result.setClass(this, EBFragmentActivity::class.java)
    fragmentClass?.let { result.putExtra(EBFragmentActivity.KEY_FRAGMENT_CLASS, it) }
    fragmentArguments?.let { result.putExtra(EBFragmentActivity.KEY_FRAGMENT_ARGUMENTS, it) }
    return result
}

fun Context.startActivityByContext(intent: Intent, activityClass: Class<out Activity>? = null) {
    startActivity(createStartActivityIntent(intent, activityClass))
}

fun Context.startActivityByContext(activityClass: Class<out Activity>) {
    startActivity(createStartActivityIntent(null, activityClass))
}

fun Context.startFragmentByContext(
    intent: Intent,
    fragmentClass: Class<out Fragment>? = null,
    fragmentArguments: Bundle? = null
) {
    startActivity(createStartFragmentIntent(intent, fragmentClass, fragmentArguments))
}

fun Context.startFragmentByContext(fragmentClass: Class<out Fragment>, fragmentArguments: Bundle? = null) {
    startActivity(createStartFragmentIntent(null, fragmentClass, fragmentArguments))
}
