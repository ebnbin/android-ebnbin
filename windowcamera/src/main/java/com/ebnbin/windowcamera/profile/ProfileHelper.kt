package com.ebnbin.windowcamera.profile

import android.content.SharedPreferences
import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb.sharedpreferences.Sp
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.profile.enumeration.Profile
import com.ebnbin.windowcamera.profile.enumeration.ProfileGesture
import com.ebnbin.windowcamera.profile.enumeration.ProfileRatio

object ProfileHelper {
    var profile: Sp<String> = Sp("profile", Profile.DEFAULT.key)
    val page: Sp<Int> = Sp("page", 1)

    //*****************************************************************************************************************

    val layout: ProfileSp<Unit> = ProfileSp("layout") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(Unit)
            Profile.WALKING -> ProfileSp.Builder(Unit)
            Profile.MIRROR -> ProfileSp.Builder(Unit)
            Profile.CIRCLE -> ProfileSp.Builder(Unit)
            else -> ProfileSp.Builder(Unit)
        }
    }
    val size: ProfileSp<Int> = ProfileSp("size") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(35)
            Profile.WALKING -> ProfileSp.Builder(100, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(100, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(35)
            else -> ProfileSp.Builder(50)
        }
    }
    val ratio: ProfileSp<String> = ProfileSp("ratio") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(ProfileRatio.CAPTURE.entryValue)
            Profile.WALKING -> ProfileSp.Builder(ProfileRatio.SCREEN.entryValue, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(ProfileRatio.SCREEN.entryValue, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(ProfileRatio.SQUARE.entryValue, isEnabled = false, isLockedDefaultValue = true)
            else -> ProfileSp.Builder(ProfileRatio.CAPTURE.entryValue)
        }
    }
    val is_out_enabled: ProfileSp<Boolean> = ProfileSp("is_out_enabled") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(false, isEnabled = false, isLockedDefaultValue = true)
            Profile.WALKING -> ProfileSp.Builder(true, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(true, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(true)
            else -> ProfileSp.Builder(false)
        }
    }
    val is_out_enabled_off: ProfileSp<Unit> = ProfileSp("is_out_enabled_off") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(Unit)
            Profile.WALKING -> ProfileSp.Builder(Unit, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(Unit, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(Unit)
            else -> ProfileSp.Builder(Unit)
        }
    }
    val in_x: ProfileSp<Int> = ProfileSp("in_x") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(95)
            Profile.WALKING -> ProfileSp.Builder(50, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(50, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(50)
            else -> ProfileSp.Builder(50)
        }
    }
    val in_y: ProfileSp<Int> = ProfileSp("in_y") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(5)
            Profile.WALKING -> ProfileSp.Builder(50, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(50, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(50)
            else -> ProfileSp.Builder(50)
        }
    }
    val is_out_enabled_on: ProfileSp<Unit> = ProfileSp("is_out_enabled_on") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(Unit, isEnabled = false, isLockedDefaultValue = true)
            Profile.WALKING -> ProfileSp.Builder(Unit, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(Unit, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(Unit)
            else -> ProfileSp.Builder(Unit)
        }
    }
    val out_x: ProfileSp<Int> = ProfileSp("out_x") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(50, isEnabled = false, isLockedDefaultValue = true)
            Profile.WALKING -> ProfileSp.Builder(50, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(50, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(50)
            else -> ProfileSp.Builder(50)
        }
    }
    val out_y: ProfileSp<Int> = ProfileSp("out_y") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(50, isEnabled = false, isLockedDefaultValue = true)
            Profile.WALKING -> ProfileSp.Builder(50, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(50, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(50)
            else -> ProfileSp.Builder(50)
        }
    }
    val display: ProfileSp<Unit> = ProfileSp("display") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(Unit)
            Profile.WALKING -> ProfileSp.Builder(Unit)
            Profile.MIRROR -> ProfileSp.Builder(Unit)
            Profile.CIRCLE -> ProfileSp.Builder(Unit)
            else -> ProfileSp.Builder(Unit)
        }
    }
    val alpha: ProfileSp<Int> = ProfileSp("alpha") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(100)
            Profile.WALKING -> ProfileSp.Builder(25)
            Profile.MIRROR -> ProfileSp.Builder(100, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(100)
            else -> ProfileSp.Builder(100)
        }
    }
    val radius: ProfileSp<Int> = ProfileSp("radius") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(15)
            Profile.WALKING -> ProfileSp.Builder(0, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(0, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(100, isEnabled = false, isLockedDefaultValue = true)
            else -> ProfileSp.Builder(0)
        }
    }
    val is_border_enabled: ProfileSp<Boolean> = ProfileSp("is_border_enabled") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(true)
            Profile.WALKING -> ProfileSp.Builder(false)
            Profile.MIRROR -> ProfileSp.Builder(true)
            Profile.CIRCLE -> ProfileSp.Builder(true)
            else -> ProfileSp.Builder(true)
        }
    }
    val is_border_enabled_on: ProfileSp<Unit> = ProfileSp("is_border_enabled_on") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(Unit)
            Profile.WALKING -> ProfileSp.Builder(Unit)
            Profile.MIRROR -> ProfileSp.Builder(Unit)
            Profile.CIRCLE -> ProfileSp.Builder(Unit)
            else -> ProfileSp.Builder(Unit)
        }
    }
    val border_width: ProfileSp<Int> = ProfileSp("border_width") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(2)
            Profile.WALKING -> ProfileSp.Builder(2)
            Profile.MIRROR -> ProfileSp.Builder(2)
            Profile.CIRCLE -> ProfileSp.Builder(2)
            else -> ProfileSp.Builder(2)
        }
    }
    val is_keep_screen_on_enabled: ProfileSp<Boolean> = ProfileSp("is_keep_screen_on_enabled") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(true)
            Profile.WALKING -> ProfileSp.Builder(false)
            Profile.MIRROR -> ProfileSp.Builder(true)
            Profile.CIRCLE -> ProfileSp.Builder(true)
            else -> ProfileSp.Builder(true)
        }
    }
    val toast: ProfileSp<String> = ProfileSp("toast") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder("system_alert_window")
            Profile.WALKING -> ProfileSp.Builder("none")
            Profile.MIRROR -> ProfileSp.Builder("system_alert_window")
            Profile.CIRCLE -> ProfileSp.Builder("system_alert_window")
            else -> ProfileSp.Builder("system_alert_window")
        }
    }
    val control: ProfileSp<Unit> = ProfileSp("control") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(Unit)
            Profile.WALKING -> ProfileSp.Builder(Unit)
            Profile.MIRROR -> ProfileSp.Builder(Unit)
            Profile.CIRCLE -> ProfileSp.Builder(Unit)
            else -> ProfileSp.Builder(Unit)
        }
    }
    val is_touchable: ProfileSp<Boolean> = ProfileSp("is_touchable") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(true)
            Profile.WALKING -> ProfileSp.Builder(false, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(true, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(true)
            else -> ProfileSp.Builder(true)
        }
    }
    val is_touchable_on: ProfileSp<Unit> = ProfileSp("is_touchable_on") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(Unit)
            Profile.WALKING -> ProfileSp.Builder(Unit, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(Unit)
            Profile.CIRCLE -> ProfileSp.Builder(Unit)
            else -> ProfileSp.Builder(Unit)
        }
    }
    val single_tap: ProfileSp<String> = ProfileSp("single_tap") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(ProfileGesture.CAPTURE.entryValue)
            Profile.WALKING -> ProfileSp.Builder(ProfileGesture.NONE.entryValue, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(ProfileGesture.CAPTURE.entryValue)
            Profile.CIRCLE -> ProfileSp.Builder(ProfileGesture.CAPTURE.entryValue)
            else -> ProfileSp.Builder(ProfileGesture.CAPTURE.entryValue)
        }
    }
    val double_tap: ProfileSp<String> = ProfileSp("double_tap") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(ProfileGesture.SWITCH_IS_FRONT.entryValue)
            Profile.WALKING -> ProfileSp.Builder(ProfileGesture.NONE.entryValue, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(ProfileGesture.SWITCH_IS_PREVIEW.entryValue)
            Profile.CIRCLE -> ProfileSp.Builder(ProfileGesture.SWITCH_IS_FRONT.entryValue)
            else -> ProfileSp.Builder(ProfileGesture.SWITCH_IS_FRONT.entryValue)
        }
    }
    val long_press: ProfileSp<String> = ProfileSp("long_press") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(ProfileGesture.CLOSE_APP.entryValue)
            Profile.WALKING -> ProfileSp.Builder(ProfileGesture.NONE.entryValue, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(ProfileGesture.CLOSE_APP.entryValue, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(ProfileGesture.CLOSE_APP.entryValue)
            else -> ProfileSp.Builder(ProfileGesture.CLOSE_APP.entryValue)
        }
    }
    val is_move_enabled: ProfileSp<Boolean> = ProfileSp("is_move_enabled") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(true)
            Profile.WALKING -> ProfileSp.Builder(false, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(false, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(true)
            else -> ProfileSp.Builder(true)
        }
    }
    val is_stop_when_screen_off_enabled: ProfileSp<Boolean> = ProfileSp("is_stop_when_screen_off_enabled") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(true)
            Profile.WALKING -> ProfileSp.Builder(true)
            Profile.MIRROR -> ProfileSp.Builder(true)
            Profile.CIRCLE -> ProfileSp.Builder(true)
            else -> ProfileSp.Builder(true)
        }
    }

    //*****************************************************************************************************************

    val is_front: ProfileSp<Boolean> = ProfileSp("is_front") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(false)
            Profile.WALKING -> ProfileSp.Builder(false, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(true, isEnabled = false, isLockedDefaultValue = true)
            Profile.CIRCLE -> ProfileSp.Builder(false)
            else -> ProfileSp.Builder(false)
        }
    }
    val is_preview: ProfileSp<Boolean> = ProfileSp("is_preview") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(false)
            Profile.WALKING -> ProfileSp.Builder(true, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(false)
            Profile.CIRCLE -> ProfileSp.Builder(false)
            else -> ProfileSp.Builder(false)
        }
    }
    val is_preview_off: ProfileSp<Unit> = ProfileSp("is_preview_off") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(Unit)
            Profile.WALKING -> ProfileSp.Builder(Unit, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(Unit)
            Profile.CIRCLE -> ProfileSp.Builder(Unit)
            else -> ProfileSp.Builder(Unit)
        }
    }
    val is_video: ProfileSp<Boolean> = ProfileSp("profile_is_video") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(false)
            Profile.WALKING -> ProfileSp.Builder(false, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(false)
            Profile.CIRCLE -> ProfileSp.Builder(false)
            else -> ProfileSp.Builder(false)
        }
    }
    val back_photo: ProfileSp<Unit> = ProfileSp("back_photo") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(Unit)
            Profile.WALKING -> ProfileSp.Builder(Unit, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(Unit)
            Profile.CIRCLE -> ProfileSp.Builder(Unit)
            else -> ProfileSp.Builder(Unit)
        }
    }
    val back_photo_resolution: ProfileSp<String> = ProfileSp("back_photo_resolution") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(CameraHelper.getInstance().requireBackDevice().requireDefaultPhotoResolution().entryValue)
            Profile.WALKING -> ProfileSp.Builder(CameraHelper.getInstance().requireBackDevice().requireDefaultPhotoResolution().entryValue, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(CameraHelper.getInstance().requireBackDevice().requireDefaultPhotoResolution().entryValue)
            Profile.CIRCLE -> ProfileSp.Builder(CameraHelper.getInstance().requireBackDevice().requireDefaultPhotoResolution().entryValue)
            else -> ProfileSp.Builder(CameraHelper.getInstance().requireBackDevice().requireDefaultPhotoResolution().entryValue)
        }
    }
    val back_video: ProfileSp<Unit> = ProfileSp("back_video") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(Unit)
            Profile.WALKING -> ProfileSp.Builder(Unit, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(Unit)
            Profile.CIRCLE -> ProfileSp.Builder(Unit)
            else -> ProfileSp.Builder(Unit)
        }
    }
    val back_video_profile: ProfileSp<String> = ProfileSp("back_video_profile") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(CameraHelper.getInstance().requireBackDevice().requireDefaultVideoProfile().entryValue)
            Profile.WALKING -> ProfileSp.Builder(CameraHelper.getInstance().requireBackDevice().requireDefaultVideoProfile().entryValue, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(CameraHelper.getInstance().requireBackDevice().requireDefaultVideoProfile().entryValue)
            Profile.CIRCLE -> ProfileSp.Builder(CameraHelper.getInstance().requireBackDevice().requireDefaultVideoProfile().entryValue)
            else -> ProfileSp.Builder(CameraHelper.getInstance().requireBackDevice().requireDefaultVideoProfile().entryValue)
        }
    }
    val front_photo: ProfileSp<Unit> = ProfileSp("front_photo") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(Unit)
            Profile.WALKING -> ProfileSp.Builder(Unit, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(Unit)
            Profile.CIRCLE -> ProfileSp.Builder(Unit)
            else -> ProfileSp.Builder(Unit)
        }
    }
    val front_photo_resolution: ProfileSp<String> = ProfileSp("front_photo_resolution") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(CameraHelper.getInstance().requireFrontDevice().requireDefaultPhotoResolution().entryValue)
            Profile.WALKING -> ProfileSp.Builder(CameraHelper.getInstance().requireFrontDevice().requireDefaultPhotoResolution().entryValue, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(CameraHelper.getInstance().requireFrontDevice().requireDefaultPhotoResolution().entryValue)
            Profile.CIRCLE -> ProfileSp.Builder(CameraHelper.getInstance().requireFrontDevice().requireDefaultPhotoResolution().entryValue)
            else -> ProfileSp.Builder(CameraHelper.getInstance().requireFrontDevice().requireDefaultPhotoResolution().entryValue)
        }
    }
    val front_video: ProfileSp<Unit> = ProfileSp("front_video") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(Unit)
            Profile.WALKING -> ProfileSp.Builder(Unit, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(Unit)
            Profile.CIRCLE -> ProfileSp.Builder(Unit)
            else -> ProfileSp.Builder(Unit)
        }
    }
    val front_video_profile: ProfileSp<String> = ProfileSp("front_video_profile") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(CameraHelper.getInstance().requireFrontDevice().requireDefaultVideoProfile().entryValue)
            Profile.WALKING -> ProfileSp.Builder(CameraHelper.getInstance().requireFrontDevice().requireDefaultVideoProfile().entryValue, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(CameraHelper.getInstance().requireFrontDevice().requireDefaultVideoProfile().entryValue)
            Profile.CIRCLE -> ProfileSp.Builder(CameraHelper.getInstance().requireFrontDevice().requireDefaultVideoProfile().entryValue)
            else -> ProfileSp.Builder(CameraHelper.getInstance().requireFrontDevice().requireDefaultVideoProfile().entryValue)
        }
    }

    //*****************************************************************************************************************

    val path: ProfileSp<Unit> = ProfileSp("path") {
        when (Profile.get()) {
            Profile.DEFAULT -> ProfileSp.Builder(Unit)
            Profile.WALKING -> ProfileSp.Builder(Unit, isEnabled = false, isLockedDefaultValue = true)
            Profile.MIRROR -> ProfileSp.Builder(Unit)
            Profile.CIRCLE -> ProfileSp.Builder(Unit)
            else -> ProfileSp.Builder(Unit)
        }
    }

    //*****************************************************************************************************************

    fun x(): ProfileSp<Int> {
        return if (is_out_enabled.value) out_x else in_x
    }

    fun y(): ProfileSp<Int> {
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
        return if (is_front.value) {
            CameraHelper.getInstance().requireFrontDevice()
        } else {
            CameraHelper.getInstance().requireBackDevice()
        }
    }

    fun photoResolution(): CameraHelper.Device.Resolution {
        if (is_video.value) throw RuntimeException()
        return if (is_front.value) {
            CameraHelper.getInstance().requireFrontDevice().getPhotoResolution(front_photo_resolution.value)
        } else {
            CameraHelper.getInstance().requireBackDevice().getPhotoResolution(back_photo_resolution.value)
        }
    }

    fun videoProfile(): CameraHelper.Device.VideoProfile {
        if (!is_video.value) throw RuntimeException()
        return if (is_front.value) {
            CameraHelper.getInstance().requireFrontDevice().getVideoProfile(front_video_profile.value)
        } else {
            CameraHelper.getInstance().requireBackDevice().getVideoProfile(back_video_profile.value)
        }
    }

    fun resolution(): CameraHelper.Device.Resolution {
        return if (is_preview.value) {
            device().sensorResolution
        } else {
            if (is_video.value) {
                videoProfile()
            } else {
                photoResolution()
            }
        }
    }

    fun previewResolution(): CameraHelper.Device.Resolution {
        return device().getPreviewResolution(resolution())
    }

    //*****************************************************************************************************************

    fun getSharedPreferencesNamePostfix(): String {
        return "_profile_${profile.value}"
    }

    fun sharedPreferencesRegister(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        SharedPreferencesHelper.get(getSharedPreferencesNamePostfix())
            .registerOnSharedPreferenceChangeListener(listener)
    }

    fun sharedPreferencesUnregister(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        SharedPreferencesHelper.get(getSharedPreferencesNamePostfix())
            .unregisterOnSharedPreferenceChangeListener(listener)
    }

    var cameraState: CameraState = CameraState.CLOSED
        set(value) {
            if (field == value) return
            field = value
            Libraries.eventBus.post(CameraStateEvent(field))
        }
}
