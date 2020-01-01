package com.ebnbin.eb.dev

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment

class CreateDevPage(
    private val title: CharSequence,
    private val fragmentClass: Class<out Fragment>,
    private val createFragmentArguments: ((Activity) -> Bundle)? = null
) {
    fun create(activity: Activity): DevPage {
        val fragmentArguments = Bundle()
        createFragmentArguments?.invoke(activity)?.let {
            fragmentArguments.putAll(it)
        }
        if (activity is DevCallback) {
            activity.createDevFragmentArguments().let {
                fragmentArguments.putAll(it)
            }
        }
        return DevPage(title, fragmentClass, fragmentArguments)
    }
}
