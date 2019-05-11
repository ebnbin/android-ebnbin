package com.ebnbin.windowcamera.profile

import android.content.SharedPreferences
import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb.sharedpreferences.Sp
import com.ebnbin.eb.util.res
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper

object ProfileHelper {
    val layout: Sp<Unit> = ProfileSp(R.string.profile_layout, Unit)
    val size: Sp<Int> = ProfileSp(R.string.profile_size, res.getInteger(R.integer.profile_size_default_value))
    val ratio: Sp<String> = ProfileSp(R.string.profile_ratio, res.getString(R.string.profile_ratio_default_value))
    val is_out_enabled: Sp<Boolean> = ProfileSp(R.string.profile_is_out_enabled, res.getBoolean(R.bool.profile_is_out_enabled_default_value))
    val is_out_enabled_off: Sp<Unit> = ProfileSp(R.string.profile_is_out_enabled_off, Unit)
    val in_x: Sp<Int> = ProfileSp(R.string.profile_in_x, res.getInteger(R.integer.profile_in_x_default_value))
    val in_y: Sp<Int> = ProfileSp(R.string.profile_in_y, res.getInteger(R.integer.profile_in_y_default_value))
    val is_out_enabled_on: Sp<Unit> = ProfileSp(R.string.profile_is_out_enabled_on, Unit)
    val out_x: Sp<Int> = ProfileSp(R.string.profile_out_x, res.getInteger(R.integer.profile_out_x_default_value))
    val out_y: Sp<Int> = ProfileSp(R.string.profile_out_y, res.getInteger(R.integer.profile_out_y_default_value))
    val display: Sp<Unit> = ProfileSp(R.string.profile_display, Unit)
    val alpha: Sp<Int> = ProfileSp(R.string.profile_alpha, res.getInteger(R.integer.profile_alpha_default_value))
    val is_keep_screen_on_enabled: Sp<Boolean> = ProfileSp(R.string.profile_is_keep_screen_on_enabled, res.getBoolean(R.bool.profile_is_keep_screen_on_enabled_default_value))
    val control: Sp<Unit> = ProfileSp(R.string.profile_control, Unit)
    val is_touchable: Sp<Boolean> = ProfileSp(R.string.profile_is_touchable, res.getBoolean(R.bool.profile_is_touchable_default_value))
    val is_touchable_on: Sp<Unit> = ProfileSp(R.string.profile_is_touchable_on, Unit)
    val single_tap: Sp<Unit> = ProfileSp(R.string.profile_single_tap, Unit)
    val double_tap: Sp<Unit> = ProfileSp(R.string.profile_double_tap, Unit)
    val long_press: Sp<Unit> = ProfileSp(R.string.profile_long_press, Unit)
    val is_move_enabled: Sp<Boolean> = ProfileSp(R.string.profile_is_move_enabled, res.getBoolean(R.bool.profile_is_move_enabled_default_value))

    val is_front: Sp<Boolean> = ProfileSp(R.string.profile_is_front, res.getBoolean(R.bool.profile_is_front_default_value))
    val is_video: Sp<Boolean> = ProfileSp(R.string.profile_is_video, res.getBoolean(R.bool.profile_is_video_default_value))
    val back_photo: Sp<Unit> = ProfileSp(R.string.profile_back_photo, Unit)
    val back_photo_resolution: Sp<String> = ProfileSp(R.string.profile_back_photo_resolution) { CameraHelper.instance.backDevice.defaultPhotoResolution.entryValue }
    val back_video: Sp<Unit> = ProfileSp(R.string.profile_back_video, Unit)
    val back_video_profile: Sp<String> = ProfileSp(R.string.profile_back_video_profile) { CameraHelper.instance.backDevice.defaultVideoProfile.entryValue }
    val front_photo: Sp<Unit> = ProfileSp(R.string.profile_front_photo, Unit)
    val front_photo_resolution: Sp<String> = ProfileSp(R.string.profile_front_photo_resolution) { CameraHelper.instance.frontDevice.defaultPhotoResolution.entryValue }
    val front_video: Sp<Unit> = ProfileSp(R.string.profile_front_video, Unit)
    val front_video_profile: Sp<String> = ProfileSp(R.string.profile_front_video_profile) { CameraHelper.instance.frontDevice.defaultVideoProfile.entryValue }

    val path: Sp<Unit> = ProfileSp(R.string.profile_path, Unit)

    //*****************************************************************************************************************

    val page: Sp<Int> = ProfileSp("page") { 1 }

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
        return if (is_front.value) CameraHelper.instance.frontDevice else CameraHelper.instance.backDevice
    }

    fun photoResolution(): CameraHelper.Device.Resolution {
        if (is_video.value) throw RuntimeException()
        return if (is_front.value) {
            CameraHelper.instance.frontDevice.getPhotoResolution(front_photo_resolution.value)
        } else {
            CameraHelper.instance.backDevice.getPhotoResolution(back_photo_resolution.value)
        }
    }

    fun videoProfile(): CameraHelper.Device.VideoProfile {
        if (!is_video.value) throw RuntimeException()
        return if (is_front.value) {
            CameraHelper.instance.frontDevice.getVideoProfile(front_video_profile.value)
        } else {
            CameraHelper.instance.backDevice.getVideoProfile(back_video_profile.value)
        }
    }

    fun resolution(): CameraHelper.Device.Resolution {
        return if (is_video.value) videoProfile() else photoResolution()
    }

    fun previewResolution(): CameraHelper.Device.Resolution {
        return device().defaultPreviewResolution
    }

    //*****************************************************************************************************************

    const val SHARED_PREFERENCES_NAME_POSTFIX: String = "_profile_default"

    fun sharedPreferencesRegister(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        SharedPreferencesHelper.register(listener, SHARED_PREFERENCES_NAME_POSTFIX)
    }

    fun sharedPreferencesUnregister(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        SharedPreferencesHelper.unregister(listener, SHARED_PREFERENCES_NAME_POSTFIX)
    }

    var cameraState: CameraState = CameraState.CLOSED
        set(value) {
            if (field == value) return
            field = value
            Libraries.eventBus.post(CameraStateEvent(field))
        }
}
