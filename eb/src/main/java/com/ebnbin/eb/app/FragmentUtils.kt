package com.ebnbin.eb.app

import androidx.fragment.app.Fragment

/**
 * 将自己从 FragmentManager 移除.
 */
fun Fragment.removeSelf() {
    fragmentManager?.beginTransaction()?.remove(this)?.commit()
}
