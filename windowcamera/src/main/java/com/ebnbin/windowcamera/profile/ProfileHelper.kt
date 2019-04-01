package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.library.eventBus
import com.ebnbin.eb.sharedpreferences.delegate.SharedPreferencesDelegate
import com.ebnbin.windowcamera.event.CameraProfileEvent

object ProfileHelper {
    const val KEY_IS_FRONT: String = "is_front"
    const val DEF_VALUE_IS_FRONT: Boolean = false

    var isFront: Boolean by SharedPreferencesDelegate(KEY_IS_FRONT, DEF_VALUE_IS_FRONT)

    var isCameraProfileInvalidating: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            eventBus.post(CameraProfileEvent(field))
        }
}
