package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.sharedpreferences.sp.Sp
import com.ebnbin.windowcamera.camera.CameraHelper

object ProfileSpManager {
    val page: Sp<Int> = ProfileSp("page", 1)

    val layout: Sp<Unit> = ProfileSp("layout", Unit)
    val size: Sp<Int> = ProfileSp("size", 50)
    val ratio: Sp<String> = ProfileSp("ratio", "capture")
    val is_out_enabled: Sp<Boolean> = ProfileSp("is_out_enabled", false)
    val is_out_enabled_off: Sp<Unit> = ProfileSp("is_out_enabled_off", Unit)
    val in_x: Sp<Int> = ProfileSp("in_x", 50)
    val in_y: Sp<Int> = ProfileSp("in_y", 50)
    val is_out_enabled_on: Sp<Unit> = ProfileSp("is_out_enabled_on", Unit)
    val out_x: Sp<Int> = ProfileSp("out_x", 50)
    val out_y: Sp<Int> = ProfileSp("out_y", 50)
    val display: Sp<Unit> = ProfileSp("display", Unit)
    val alpha: Sp<Int> = ProfileSp("alpha", 100)
    val is_keep_screen_on_enabled: Sp<Boolean> = ProfileSp("is_keep_screen_on_enabled", true)
    val control: Sp<Unit> = ProfileSp("control", Unit)
    val is_touchable: Sp<Boolean> = ProfileSp("is_touchable", true)
    val is_touchable_on: Sp<Unit> = ProfileSp("is_touchable_on", Unit)
    val gesture: Sp<Unit> = ProfileSp("gesture", Unit)
    val single_tap: Sp<Unit> = ProfileSp("single_tap", Unit)
    val double_tap: Sp<Unit> = ProfileSp("double_tap", Unit)
    val long_press: Sp<Unit> = ProfileSp("long_press", Unit)
    val is_move_enabled: Sp<Boolean> = ProfileSp("is_move_enabled", true)

    val is_front: Sp<Boolean> = ProfileSp("is_front", false)
    val is_video: Sp<Boolean> = ProfileSp("is_video", false)
    val back_photo: Sp<Unit> = ProfileSp("back_photo", Unit)
    val back_photo_resolution: Sp<String> = ProfileSp("back_photo_resolution",
        CameraHelper.backDevice.photoResolutions.first().key)
    val back_video: Sp<Unit> = ProfileSp("back_video", Unit)
    val back_video_profile: Sp<String> = ProfileSp("back_video_profile",
        CameraHelper.backDevice.videoProfiles.first().key)
    val front_photo: Sp<Unit> = ProfileSp("front_photo", Unit)
    val front_photo_resolution: Sp<String> = ProfileSp("front_photo_resolution",
        CameraHelper.frontDevice.photoResolutions.first().key)
    val front_video: Sp<Unit> = ProfileSp("front_video", Unit)
    val front_video_profile: Sp<String> = ProfileSp("front_video_profile",
        CameraHelper.frontDevice.videoProfiles.first().key)

    val path: Sp<Unit> = ProfileSp("path", Unit)

    //*****************************************************************************************************************

    fun x(): Sp<Int> {
        return if (is_out_enabled.value) out_x else in_x
    }

    fun y(): Sp<Int> {
        return if (is_out_enabled.value) out_y else in_y
    }

    fun putXY(x: Int, y: Int) {
        if (is_out_enabled.value) {
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
}
