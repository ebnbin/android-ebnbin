package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.library.eventBus
import com.ebnbin.eb.sharedpreferences.SpHelper
import com.ebnbin.eb.sharedpreferences.SpItem
import com.ebnbin.windowcamera.camera.CameraHelper

object ProfileHelper {
    val page: SpItem<Int> = ProfileSpItem("page", 1)

    val layout: SpItem<Unit> = ProfileSpItem("layout", Unit)
    val size: SpItem<Int> = ProfileSpItem("size", 50)
    val ratio: SpItem<String> = ProfileSpItem("ratio", "capture")
    val is_out_enabled: SpItem<Boolean> = ProfileSpItem("is_out_enabled", false)
    val is_out_enabled_off: SpItem<Unit> = ProfileSpItem("is_out_enabled_off", Unit)
    val in_x: SpItem<Int> = ProfileSpItem("in_x", 50)
    val in_y: SpItem<Int> = ProfileSpItem("in_y", 50)
    val is_out_enabled_on: SpItem<Unit> = ProfileSpItem("is_out_enabled_on", Unit)
    val out_x: SpItem<Int> = ProfileSpItem("out_x", 50)
    val out_y: SpItem<Int> = ProfileSpItem("out_y", 50)
    val display: SpItem<Unit> = ProfileSpItem("display", Unit)
    val alpha: SpItem<Int> = ProfileSpItem("alpha", 100)
    val is_keep_screen_on_enabled: SpItem<Boolean> = ProfileSpItem("is_keep_screen_on_enabled", true)
    val control: SpItem<Unit> = ProfileSpItem("control", Unit)
    val is_touchable: SpItem<Boolean> = ProfileSpItem("is_touchable", true)
    val is_touchable_on: SpItem<Unit> = ProfileSpItem("is_touchable_on", Unit)
    val gesture: SpItem<Unit> = ProfileSpItem("gesture", Unit)
    val single_tap: SpItem<Unit> = ProfileSpItem("single_tap", Unit)
    val double_tap: SpItem<Unit> = ProfileSpItem("double_tap", Unit)
    val long_press: SpItem<Unit> = ProfileSpItem("long_press", Unit)
    val is_move_enabled: SpItem<Boolean> = ProfileSpItem("is_move_enabled", true)

    val is_front: SpItem<Boolean> = ProfileSpItem("is_front", false)
    val is_video: SpItem<Boolean> = ProfileSpItem("is_video", false)
    val back_photo: SpItem<Unit> = ProfileSpItem("back_photo", Unit)
    val back_photo_resolution: SpItem<String> = ProfileSpItem("back_photo_resolution",
        CameraHelper.backDevice.photoResolutions.first().key)
    val back_video: SpItem<Unit> = ProfileSpItem("back_video", Unit)
    val back_video_profile: SpItem<String> = ProfileSpItem("back_video_profile",
        CameraHelper.backDevice.videoProfiles.first().key)
    val front_photo: SpItem<Unit> = ProfileSpItem("front_photo", Unit)
    val front_photo_resolution: SpItem<String> = ProfileSpItem("front_photo_resolution",
        CameraHelper.frontDevice.photoResolutions.first().key)
    val front_video: SpItem<Unit> = ProfileSpItem("front_video", Unit)
    val front_video_profile: SpItem<String> = ProfileSpItem("front_video_profile",
        CameraHelper.frontDevice.videoProfiles.first().key)

    val path: SpItem<Unit> = ProfileSpItem("path", Unit)

    //*****************************************************************************************************************

    val sharedPreferencesName: String
        get() = "${SpHelper.defaultSharedPreferencesName}_profile_default"

    fun x(isOutEnabled: Boolean): Int {
        return if (isOutEnabled) out_x.value else in_x.value
    }

    fun y(isOutEnabled: Boolean): Int {
        return if (isOutEnabled) out_y.value else in_y.value
    }

    fun putXY(x: Int, y: Int, isOutEnabled: Boolean) {
        if (isOutEnabled) {
            out_x.value = x
            out_y.value = y
        } else {
            in_x.value = x
            in_y.value = y
        }
    }

    fun device(): CameraHelper.Device {
        return if (is_front.value) CameraHelper.frontDevice else CameraHelper.backDevice
    }

    fun photoResolution(): CameraHelper.Device.Resolution? {
        if (is_video.value) return null
        return if (is_front.value) {
            CameraHelper.frontDevice.getPhotoResolutionOrNull(front_photo_resolution.value)
        } else {
            CameraHelper.backDevice.getPhotoResolutionOrNull(back_photo_resolution.value)
        }
    }

    fun videoProfile(): CameraHelper.Device.VideoProfile? {
        if (!is_video.value) return null
        return if (is_front.value) {
            CameraHelper.frontDevice.getVideoProfileOrNull(front_video_profile.value)
        } else {
            CameraHelper.backDevice.getVideoProfileOrNull(back_video_profile.value)
        }
    }

    var isCameraProfileInvalidating: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            eventBus.post(CameraProfileEvent(field))
        }
}
