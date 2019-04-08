package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.library.eventBus
import com.ebnbin.eb.sharedpreferences.delegate.SharedPreferencesDelegate
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.event.CameraProfileEvent

object ProfileHelper {
    const val KEY_SIZE: String = "size"
    const val DEF_VALUE_SIZE: Int = 50
    var size: Int by SharedPreferencesDelegate(KEY_SIZE, DEF_VALUE_SIZE)

    const val KEY_RATIO: String = "ratio"
    const val DEF_VALUE_RATIO: String = "capture"
    var ratio: String by SharedPreferencesDelegate(KEY_RATIO, DEF_VALUE_RATIO)

    const val KEY_IS_FRONT: String = "is_front"
    const val DEF_VALUE_IS_FRONT: Boolean = false
    var isFront: Boolean by SharedPreferencesDelegate(KEY_IS_FRONT, DEF_VALUE_IS_FRONT)

    fun device(): CameraHelper.Device {
        return if (isFront) CameraHelper.frontDevice else CameraHelper.backDevice
    }

    var isCameraProfileInvalidating: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            eventBus.post(CameraProfileEvent(field))
        }
}
