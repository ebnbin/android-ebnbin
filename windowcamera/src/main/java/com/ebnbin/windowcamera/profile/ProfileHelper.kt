package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.library.eventBus
import com.ebnbin.eb.sharedpreferences.defaultSharedPreferencesName
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.event.CameraProfileEvent

object ProfileHelper {
    val sharedPreferencesName: String
        get() = "${defaultSharedPreferencesName}_profile_default"

    var page: Int by ProfileSharedPreferencesDelegate("page", 1)

    const val KEY_LAYOUT: String = "layout"

    const val KEY_SIZE: String = "size"
    const val DEF_VALUE_SIZE: Int = 50
    var size: Int by ProfileSharedPreferencesDelegate(KEY_SIZE, DEF_VALUE_SIZE)

    const val KEY_RATIO: String = "ratio"
    const val DEF_VALUE_RATIO: String = "capture"
    var ratio: String by ProfileSharedPreferencesDelegate(KEY_RATIO, DEF_VALUE_RATIO)

    const val KEY_X: String = "x"
    const val DEF_VALUE_X: Int = 50
    var x: Int by ProfileSharedPreferencesDelegate(KEY_X, DEF_VALUE_X)

    const val KEY_Y: String = "y"
    const val DEF_VALUE_Y: Int = 50
    var y: Int by ProfileSharedPreferencesDelegate(KEY_Y, DEF_VALUE_Y)

    const val KEY_IS_OUT_ENABLED: String = "is_out_enabled"
    const val DEF_VALUE_IS_OUT_ENABLED: Boolean = true
    var isOutEnabled: Boolean by ProfileSharedPreferencesDelegate(KEY_IS_OUT_ENABLED, DEF_VALUE_IS_OUT_ENABLED)

    const val KEY_DISPLAY: String = "display"

    const val KEY_ALPHA: String = "alpha"
    const val DEF_VALUE_ALPHA: Int = 100
    var alpha: Int by ProfileSharedPreferencesDelegate(KEY_ALPHA, DEF_VALUE_ALPHA)

    const val KEY_CONTROL: String = "control"

    const val KEY_IS_TOUCHABLE: String = "is_touchable"
    const val DEF_VALUE_IS_TOUCHABLE: Boolean = true
    var isTouchable: Boolean by ProfileSharedPreferencesDelegate(KEY_IS_TOUCHABLE, DEF_VALUE_IS_TOUCHABLE)

    const val KEY_GESTURE: String = "gesture"

    const val KEY_IS_MOVE_ENABLED: String = "is_move_enabled"
    const val DEF_VALUE_IS_MOVE_ENABLED: Boolean = true
    var isMoveEnabled: Boolean by ProfileSharedPreferencesDelegate(KEY_IS_MOVE_ENABLED, DEF_VALUE_IS_MOVE_ENABLED)

    const val KEY_IS_FRONT: String = "is_front"
    const val DEF_VALUE_IS_FRONT: Boolean = false
    var isFront: Boolean by ProfileSharedPreferencesDelegate(KEY_IS_FRONT, DEF_VALUE_IS_FRONT)

    const val KEY_IS_VIDEO: String = "is_video"
    const val DEF_VALUE_IS_VIDEO: Boolean = false
    var isVideo: Boolean by ProfileSharedPreferencesDelegate(KEY_IS_VIDEO, DEF_VALUE_IS_VIDEO)

    const val KEY_BACK_PHOTO: String = "back_photo"

    const val KEY_BACK_PHOTO_RESOLUTION: String = "back_photo_resolution"
    val DEF_VALUE_BACK_PHOTO_RESOLUTION: String = CameraHelper.backDevice.photoResolutions.first().toString()
    var backPhotoResolution: String by ProfileSharedPreferencesDelegate(KEY_BACK_PHOTO_RESOLUTION,
        DEF_VALUE_BACK_PHOTO_RESOLUTION)

    const val KEY_BACK_VIDEO: String = "back_video"

    const val KEY_BACK_VIDEO_PROFILE: String = "back_video_profile"
    val DEF_VALUE_BACK_VIDEO_PROFILE: String = CameraHelper.backDevice.videoProfiles.first().toString()
    var backVideoProfile: String by ProfileSharedPreferencesDelegate(KEY_BACK_VIDEO_PROFILE,
        DEF_VALUE_BACK_VIDEO_PROFILE)

    const val KEY_FRONT_PHOTO: String = "front_photo"

    const val KEY_FRONT_PHOTO_RESOLUTION: String = "front_photo_resolution"
    val DEF_VALUE_FRONT_PHOTO_RESOLUTION: String = CameraHelper.frontDevice.photoResolutions.first().toString()
    var frontPhotoResolution: String by ProfileSharedPreferencesDelegate(KEY_FRONT_PHOTO_RESOLUTION,
        DEF_VALUE_FRONT_PHOTO_RESOLUTION)

    const val KEY_FRONT_VIDEO: String = "front_video"

    const val KEY_FRONT_VIDEO_PROFILE: String = "front_video_profile"
    val DEF_VALUE_FRONT_VIDEO_PROFILE: String = CameraHelper.frontDevice.videoProfiles.first().toString()
    var frontVideoProfile: String by ProfileSharedPreferencesDelegate(KEY_FRONT_VIDEO_PROFILE,
        DEF_VALUE_FRONT_VIDEO_PROFILE)

    fun device(): CameraHelper.Device {
        return if (isFront) CameraHelper.frontDevice else CameraHelper.backDevice
    }

    fun photoResolution(): CameraHelper.Device.Resolution? {
        if (isVideo) return null
        return if (isFront) {
            CameraHelper.frontDevice.getPhotoResolutionOrNull(frontPhotoResolution)
        } else {
            CameraHelper.backDevice.getPhotoResolutionOrNull(backPhotoResolution)
        }
    }

    fun videoProfile(): CameraHelper.Device.VideoProfile? {
        if (!isVideo) return null
        return if (isFront) {
            CameraHelper.frontDevice.getVideoProfileOrNull(frontVideoProfile)
        } else {
            CameraHelper.backDevice.getVideoProfileOrNull(backVideoProfile)
        }
    }

    var isCameraProfileInvalidating: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            eventBus.post(CameraProfileEvent(field))
        }
}
