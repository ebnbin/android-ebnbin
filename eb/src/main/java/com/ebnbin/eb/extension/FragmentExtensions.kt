package com.ebnbin.eb.extension

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

fun Fragment.startActivityByFragment(
    intent: Intent,
    activityClass: Class<out Activity>? = null,
    requestCode: Int = 0
) {
    val context = context ?: return
    startActivityForResult(context.createStartActivityIntent(intent, activityClass), requestCode)
}

fun Fragment.startActivityByFragment(activityClass: Class<out Activity>, requestCode: Int = 0) {
    val content = context ?: return
    startActivityForResult(content.createStartActivityIntent(null, activityClass), requestCode)
}

fun Fragment.startFragmentByFragment(
    intent: Intent,
    fragmentClass: Class<out Fragment>? = null,
    requestCode: Int = 0) {
    val content = context ?: return
    startActivityForResult(content.createStartFragmentIntent(intent, fragmentClass), requestCode)
}

fun Fragment.startFragmentByFragment(fragmentClass: Class<out Fragment>, requestCode: Int = 0) {
    val content = context ?: return
    startActivityForResult(content.createStartFragmentIntent(null, fragmentClass), requestCode)
}

