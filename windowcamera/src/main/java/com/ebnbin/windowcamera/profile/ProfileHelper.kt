package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.sharedpreferences.delegate.SharedPreferencesDelegate

object ProfileHelper {
    const val KEY_IS_FRONT: String = "is_front"
    const val DEF_VALUE_IS_FRONT: Boolean = false

    var isFront: Boolean by SharedPreferencesDelegate(KEY_IS_FRONT, DEF_VALUE_IS_FRONT)
}
