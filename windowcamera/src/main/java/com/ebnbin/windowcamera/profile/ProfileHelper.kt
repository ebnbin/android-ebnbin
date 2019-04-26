package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.library.eventBus
import com.ebnbin.eb.sharedpreferences.sp.Sp
import com.ebnbin.eb.util.ebRes
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper

object ProfileHelper {
    val page: Sp<Int> = ProfileSp("page") { 1 }

    //*****************************************************************************************************************

    val layout: Sp<Unit> = ProfileSp(R.string.profile_layout) { Unit }
    val size: Sp<Int> = ProfileSp(R.string.profile_size) { ebRes.getInteger(R.integer.profile_size_default_value) }
    val ratio: Sp<String> = ProfileSp(R.string.profile_ratio) { ebRes.getString(R.string.profile_ratio_default_value) }
    val is_out_enabled: Sp<Boolean> = ProfileSp(R.string.profile_is_out_enabled) { ebRes.getBoolean(R.bool.profile_is_out_enabled_default_value) }
    val is_out_enabled_off: Sp<Unit> = ProfileSp(R.string.profile_is_out_enabled_off) { Unit }
    val in_x: Sp<Int> = ProfileSp(R.string.profile_in_x) { ebRes.getInteger(R.integer.profile_in_x_default_value) }
    val in_y: Sp<Int> = ProfileSp(R.string.profile_in_y) { ebRes.getInteger(R.integer.profile_in_y_default_value) }
    val is_out_enabled_on: Sp<Unit> = ProfileSp(R.string.profile_is_out_enabled_on) { Unit }
    val out_x: Sp<Int> = ProfileSp(R.string.profile_out_x) { ebRes.getInteger(R.integer.profile_out_x_default_value) }
    val out_y: Sp<Int> = ProfileSp(R.string.profile_out_y) { ebRes.getInteger(R.integer.profile_out_y_default_value) }
    val display: Sp<Unit> = ProfileSp(R.string.profile_display) { Unit }
    val alpha: Sp<Int> = ProfileSp(R.string.profile_alpha) { ebRes.getInteger(R.integer.profile_alpha_default_value) }
    val is_keep_screen_on_enabled: Sp<Boolean> = ProfileSp(R.string.profile_is_keep_screen_on_enabled) { ebRes.getBoolean(R.bool.profile_is_keep_screen_on_enabled_default_value) }
    val control: Sp<Unit> = ProfileSp(R.string.profile_control) { Unit }
    val is_touchable: Sp<Boolean> = ProfileSp(R.string.profile_is_touchable) { ebRes.getBoolean(R.bool.profile_is_touchable_default_value) }
    val is_touchable_on: Sp<Unit> = ProfileSp(R.string.profile_is_touchable_on) { Unit }
    val single_tap: Sp<Unit> = ProfileSp(R.string.profile_single_tap) { Unit }
    val double_tap: Sp<Unit> = ProfileSp(R.string.profile_double_tap) { Unit }
    val long_press: Sp<Unit> = ProfileSp(R.string.profile_long_press) { Unit }
    val is_move_enabled: Sp<Boolean> = ProfileSp(R.string.profile_is_move_enabled) { ebRes.getBoolean(R.bool.profile_is_move_enabled_default_value) }

    val is_front: Sp<Boolean> = ProfileSp(R.string.profile_is_front) { ebRes.getBoolean(R.bool.profile_is_front_default_value) }
    val is_video: Sp<Boolean> = ProfileSp(R.string.profile_is_video) { ebRes.getBoolean(R.bool.profile_is_video_default_value) }
    val back_photo: Sp<Unit> = ProfileSp(R.string.profile_back_photo) { Unit }
    val back_photo_resolution: Sp<String> = ProfileSp(R.string.profile_back_photo_resolution) { CameraHelper.backDevice.photoResolutions.first().key }
    val back_video: Sp<Unit> = ProfileSp(R.string.profile_back_video) { Unit }
    val back_video_profile: Sp<String> = ProfileSp(R.string.profile_back_video_profile) { CameraHelper.backDevice.videoProfiles.first().key }
    val front_photo: Sp<Unit> = ProfileSp(R.string.profile_front_photo) { Unit }
    val front_photo_resolution: Sp<String> = ProfileSp(R.string.profile_front_photo_resolution) { CameraHelper.frontDevice.photoResolutions.first().key }
    val front_video: Sp<Unit> = ProfileSp(R.string.profile_front_video) { Unit }
    val front_video_profile: Sp<String> = ProfileSp(R.string.profile_front_video_profile) { CameraHelper.frontDevice.videoProfiles.first().key }

    val path: Sp<Unit> = ProfileSp(R.string.profile_path) { Unit }

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

    //*****************************************************************************************************************

    const val sharedPreferencesNamePostfix: String = "_profile_default"

    var isCameraProfileInvalidating: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            eventBus.post(CameraProfileEvent(field))
        }
}
