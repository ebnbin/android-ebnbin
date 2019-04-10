package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.library.eventBus
import com.ebnbin.eb.sharedpreferences.defaultSharedPreferencesName
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.event.CameraProfileEvent
import com.ebnbin.windowcamera.sharedpreferences.SpHelper

object ProfileHelper {
    val sharedPreferencesName: String
        get() = "${defaultSharedPreferencesName}_profile_${SpHelper.profile}"

    const val KEY_LAYOUT: String = "layout"

    const val KEY_SIZE: String = "size"
    const val DEF_VALUE_SIZE: Int = 50
    var size: Int by ProfileSharedPreferencesDelegate(KEY_SIZE, DEF_VALUE_SIZE)

    const val KEY_RATIO: String = "ratio"
    const val DEF_VALUE_RATIO: String = "capture"
    var ratio: String by ProfileSharedPreferencesDelegate(KEY_RATIO, DEF_VALUE_RATIO)

    const val KEY_IS_OUT_ENABLED: String = "is_out_enabled"
    const val DEF_VALUE_IS_OUT_ENABLED: Boolean = false
    var isOutEnabled: Boolean by ProfileSharedPreferencesDelegate(KEY_IS_OUT_ENABLED, DEF_VALUE_IS_OUT_ENABLED)

    const val KEY_IS_OUT_ENABLED_OFF: String = "is_out_enabled_off"

    const val KEY_IN_X: String = "in_x"
    const val DEF_VALUE_IN_X: Int = 50
    var inX: Int by ProfileSharedPreferencesDelegate(KEY_IN_X, DEF_VALUE_IN_X)

    const val KEY_IN_Y: String = "in_y"
    const val DEF_VALUE_IN_Y: Int = 50
    var inY: Int by ProfileSharedPreferencesDelegate(KEY_IN_Y, DEF_VALUE_IN_Y)

    const val KEY_IS_OUT_ENABLED_ON: String = "is_out_enabled_on"

    const val KEY_OUT_X: String = "out_x"
    const val DEF_VALUE_OUT_X: Int = 50
    var outX: Int by ProfileSharedPreferencesDelegate(KEY_OUT_X, DEF_VALUE_OUT_X)

    const val KEY_OUT_Y: String = "out_y"
    const val DEF_VALUE_OUT_Y: Int = 50
    var outY: Int by ProfileSharedPreferencesDelegate(KEY_OUT_Y, DEF_VALUE_OUT_Y)

    fun x(isOutEnabled: Boolean): Int {
        return if (isOutEnabled) outX else inX
    }

    fun y(isOutEnabled: Boolean): Int {
        return if (isOutEnabled) outY else inY
    }

    fun putXY(x: Int, y: Int, isOutEnabled: Boolean) {
        if (isOutEnabled) {
            outX = x
            outY = y
        } else {
            inX = x
            inY = y
        }
    }

    const val KEY_IS_FRONT: String = "is_front"
    const val DEF_VALUE_IS_FRONT: Boolean = false
    var isFront: Boolean by ProfileSharedPreferencesDelegate(KEY_IS_FRONT, DEF_VALUE_IS_FRONT)

    const val KEY_IS_PREVIEW_ONLY: String = "is_preview_only"
    const val DEF_VALUE_IS_PREVIEW_ONLY: Boolean = false
    var isPreviewOnly: Boolean by ProfileSharedPreferencesDelegate(KEY_IS_PREVIEW_ONLY, DEF_VALUE_IS_PREVIEW_ONLY)

    const val KEY_IS_VIDEO: String = "is_video"
    const val DEF_VALUE_IS_VIDEO: Boolean = false
    var isVideo: Boolean by ProfileSharedPreferencesDelegate(KEY_IS_VIDEO, DEF_VALUE_IS_VIDEO)

    const val KEY_BACK_PHOTO: String = "back_photo"

    const val KEY_BACK_VIDEO: String = "back_video"

    const val KEY_BACK_PREVIEW: String = "back_preview"

    const val KEY_FRONT_PHOTO: String = "front_photo"

    const val KEY_FRONT_VIDEO: String = "front_video"

    const val KEY_FRONT_PREVIEW: String = "front_preview"

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
