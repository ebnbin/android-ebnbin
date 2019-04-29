package com.ebnbin.windowcamera.view

import android.content.SharedPreferences
import com.ebnbin.windowcamera.profile.ProfileHelper

class WindowCameraViewSharedPreferencesDelegate(private val windowCameraView: WindowCameraView) :
    SharedPreferences.OnSharedPreferenceChangeListener {
    fun init() {
        ProfileHelper.sharedPreferencesRegister(this)
    }

    fun dispose() {
        ProfileHelper.sharedPreferencesUnregister(this)
    }

    //*****************************************************************************************************************

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            ProfileHelper.is_front.key -> {
                windowCameraView.cameraDelegate.reopenCamera()
            }
            ProfileHelper.is_video.key -> {
                windowCameraView.cameraDelegate.reopenCamera()
            }
            ProfileHelper.back_photo_resolution.key -> {
                windowCameraView.cameraDelegate.reopenCamera()
            }
            ProfileHelper.back_video_profile.key -> {
                windowCameraView.cameraDelegate.reopenCamera()
            }
            ProfileHelper.front_photo_resolution.key -> {
                windowCameraView.cameraDelegate.reopenCamera()
            }
            ProfileHelper.front_video_profile.key -> {
                windowCameraView.cameraDelegate.reopenCamera()
            }
        }
    }
}
