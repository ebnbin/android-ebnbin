package com.ebnbin.windowcamera.view

import android.content.SharedPreferences
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.windowcamera.profile.ProfileHelper

class WindowCameraViewSharedPreferencesDelegate(private val windowCameraView: WindowCameraView) :
    SharedPreferences.OnSharedPreferenceChangeListener {
    fun init() {
        SharedPreferencesHelper.get(ProfileHelper.SHARED_PREFERENCES_NAME_POSTFIX)
            .registerOnSharedPreferenceChangeListener(this)
    }

    fun dispose() {
        SharedPreferencesHelper.get(ProfileHelper.SHARED_PREFERENCES_NAME_POSTFIX)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    //*****************************************************************************************************************

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            ProfileHelper.size.key -> {
                windowCameraView.layoutDelegate.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = true)
            }
            ProfileHelper.ratio.key -> {
                windowCameraView.layoutDelegate.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = true)
            }
            ProfileHelper.is_out_enabled.key -> {
                windowCameraView.layoutDelegate.invalidateLayout(invalidateIsOutEnabled = true, invalidateSize = true)
            }
            ProfileHelper.in_x.key -> {
                windowCameraView.layoutDelegate.invalidateLayout(
                    invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.in_y.key -> {
                windowCameraView.layoutDelegate.invalidateLayout(
                    invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.out_x.key -> {
                windowCameraView.layoutDelegate.invalidateLayout(
                    invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.out_y.key -> {
                windowCameraView.layoutDelegate.invalidateLayout(
                    invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.alpha.key -> {
                windowCameraView.layoutDelegate.invalidateAlpha()
            }
            ProfileHelper.is_keep_screen_on_enabled.key -> {
                windowCameraView.layoutDelegate.invalidateIsKeepScreenOnEnabled()
            }
            ProfileHelper.is_touchable.key -> {
                windowCameraView.layoutDelegate.invalidateIsTouchable()
            }
            ProfileHelper.is_move_enabled.key -> {
                windowCameraView.gestureDelegate.isMoveEnabled = ProfileHelper.is_move_enabled.value
            }
            ProfileHelper.is_front.key -> {
                windowCameraView.cameraDelegate.reopenCamera()
            }
            ProfileHelper.is_video.key -> {
                windowCameraView.cameraDelegate.reopenCamera()
            }
            ProfileHelper.back_photo_resolution.key -> {
                windowCameraView.cameraDelegate.reopenCamera(true)
            }
            ProfileHelper.back_video_profile.key -> {
                windowCameraView.cameraDelegate.reopenCamera(true)
            }
            ProfileHelper.front_photo_resolution.key -> {
                windowCameraView.cameraDelegate.reopenCamera(true)
            }
            ProfileHelper.front_video_profile.key -> {
                windowCameraView.cameraDelegate.reopenCamera(true)
            }
        }
    }
}
