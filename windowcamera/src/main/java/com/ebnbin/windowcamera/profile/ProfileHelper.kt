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

    const val KEY_IS_OUT_ENABLED: String = "is_out_enabled"
    const val DEF_VALUE_IS_OUT_ENABLED: Boolean = false
    var isOutEnabled: Boolean by SharedPreferencesDelegate(KEY_IS_OUT_ENABLED, DEF_VALUE_IS_OUT_ENABLED)

    const val KEY_IS_OUT_ENABLED_OFF: String = "is_out_enabled_off"

    const val KEY_IN_X: String = "in_x"
    const val DEF_VALUE_IN_X: Int = 50
    var inX: Int by SharedPreferencesDelegate(KEY_IN_X, DEF_VALUE_IN_X)

    const val KEY_IN_Y: String = "in_y"
    const val DEF_VALUE_IN_Y: Int = 50
    var inY: Int by SharedPreferencesDelegate(KEY_IN_Y, DEF_VALUE_IN_Y)

    const val KEY_IS_OUT_ENABLED_ON: String = "is_out_enabled_on"

    const val KEY_OUT_X: String = "out_x"
    const val DEF_VALUE_OUT_X: Int = 50
    var outX: Int by SharedPreferencesDelegate(KEY_OUT_X, DEF_VALUE_OUT_X)

    const val KEY_OUT_Y: String = "out_y"
    const val DEF_VALUE_OUT_Y: Int = 50
    var outY: Int by SharedPreferencesDelegate(KEY_OUT_Y, DEF_VALUE_OUT_Y)

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
