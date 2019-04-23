package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.library.eventBus

object ProfileHelper {
    const val sharedPreferencesNamePostfix: String = "_profile_default"

    var isCameraProfileInvalidating: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            eventBus.post(CameraProfileEvent(field))
        }
}
