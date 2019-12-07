package com.ebnbin.eb.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.ebnbin.eb.activity.EBFragmentActivity

internal fun Context.createStartActivityIntent(intent: Intent?, activityClass: Class<out Activity>?): Intent {
    val result = if (intent == null) Intent() else Intent(intent)
    activityClass?.let { result.setClass(this, it) }
    return result
}

internal fun Context.createStartFragmentIntent(intent: Intent?, fragmentClass: Class<out Fragment>?): Intent {
    val result = if (intent == null) Intent() else Intent(intent)
    result.setClass(this, EBFragmentActivity::class.java)
    fragmentClass?.let { result.putExtra(EBFragmentActivity.KEY_FRAGMENT_CLASS, it) }
    return result
}

fun Context.startActivityByContext(intent: Intent, activityClass: Class<out Activity>? = null) {
    startActivity(createStartActivityIntent(intent, activityClass))
}

fun Context.startActivityByContext(activityClass: Class<out Activity>) {
    startActivity(createStartActivityIntent(null, activityClass))
}

fun Context.startFragmentByContext(intent: Intent, fragmentClass: Class<out Fragment>? = null) {
    startActivity(createStartFragmentIntent(intent, fragmentClass))
}

fun Context.startFragmentByContext(fragmentClass: Class<out Fragment>) {
    startActivity(createStartFragmentIntent(null, fragmentClass))
}
