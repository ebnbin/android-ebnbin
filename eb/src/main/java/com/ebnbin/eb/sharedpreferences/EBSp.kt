package com.ebnbin.eb.sharedpreferences

import com.ebnbin.eb.sharedpreferences.delegate.EBDebugSharedPreferencesDelegate

/**
 * 偏好代理帮助类.
 */
@Suppress("ClassName")
object EBSp {
    object eb_debug {
        internal var page by EBDebugSharedPreferencesDelegate("page", 0)
    }
}
