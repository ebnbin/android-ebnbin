package com.ebnbin.eb

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.annotation.IdRes

inline fun <reified T : View> View.findOtherViewById(@IdRes id: Int, fromRoot: Boolean = false): T? {
    if (fromRoot) {
        return rootView.findViewById(id)
    }
    var currentParent: ViewParent? = if (this is ViewGroup) this else parent
    while (true) {
        if (currentParent is ViewGroup) {
            val otherView = currentParent.findViewById<T>(id)
            if (otherView != null) {
                return otherView
            }
            if (currentParent === rootView) {
                return null
            }
            currentParent = currentParent.parent
        } else {
            return null
        }
    }
}

inline fun <reified T : View> View.requireOtherViewById(@IdRes id: Int, fromRoot: Boolean = false): T {
    return findOtherViewById(id, fromRoot) ?: throw IllegalArgumentException("找不到 id.")
}
