package com.ebnbin.eb.fragment

import androidx.fragment.app.Fragment

/**
 * 将自己从 FragmentManager 中移除.
 */
fun Fragment.removeSelf() {
    fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
}
