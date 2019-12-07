package com.ebnbin.eb.extension

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

fun Activity.startActivityByActivity(
    intent: Intent,
    activityClass: Class<out Activity>? = null,
    requestCode: Int = 0
) {
    startActivityForResult(createStartActivityIntent(intent, activityClass), requestCode)
}

fun Activity.startActivityByActivity(activityClass: Class<out Activity>, requestCode: Int = 0) {
    startActivityForResult(createStartActivityIntent(null, activityClass), requestCode)
}

fun Activity.startFragmentByActivity(
    intent: Intent,
    fragmentClass: Class<out Fragment>? = null,
    requestCode: Int = 0
) {
    startActivityForResult(createStartFragmentIntent(intent, fragmentClass), requestCode)
}

fun Activity.startFragmentByActivity(fragmentClass: Class<out Fragment>, requestCode: Int = 0) {
    startActivityForResult(createStartFragmentIntent(null, fragmentClass), requestCode)
}
