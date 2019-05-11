package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.preference.PreferenceGroup
import com.ebnbin.eb.preference.SimpleListPreference
import com.ebnbin.eb.preference.SimplePreferenceGroup
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.profile.CameraState
import com.ebnbin.windowcamera.profile.CameraStateEvent
import com.ebnbin.windowcamera.profile.ProfileHelper
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CameraProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.profile_camera_fragment, rootKey)

        findPreference<SimplePreferenceGroup>(ProfileHelper.back_photo.key)?.run {
            visibleKeysOff = arrayOf(ProfileHelper.is_front.key, ProfileHelper.is_video.key)
        }

        SimpleListPreference(preferenceScreen.context).run {
            key = ProfileHelper.back_photo_resolution.key
            setDefaultValue(ProfileHelper.back_photo_resolution.getDefaultValue())
            findPreference<PreferenceGroup>(ProfileHelper.back_photo.key)?.addPreference(this)
            setTitle(R.string.profile_back_photo_resolution_title)
            entryValues = CameraHelper.instance.requireBackDevice().photoResolutions
                .map { it.entryValue }
                .toTypedArray()
            entries = CameraHelper.instance.requireBackDevice().photoResolutions
                .map {
                    getString(R.string.profile_photo_resolution_entry, it.width, it.height, it.ratioWidth,
                        it.ratioHeight, it.megapixel)
                }
                .toTypedArray()
            setIcon(R.drawable.profile_resolution)
            setDialogTitle(R.string.profile_back_photo_resolution_title)
            setDialogIcon(R.drawable.profile_resolution)
        }

        findPreference<SimplePreferenceGroup>(ProfileHelper.back_video.key)?.run {
            visibleKeysOff = arrayOf(ProfileHelper.is_front.key)
            visibleKeysOn = arrayOf(ProfileHelper.is_video.key)
        }

        SimpleListPreference(preferenceScreen.context).run {
            key = ProfileHelper.back_video_profile.key
            setDefaultValue(ProfileHelper.back_video_profile.getDefaultValue())
            findPreference<PreferenceGroup>(ProfileHelper.back_video.key)?.addPreference(this)
            setTitle(R.string.profile_back_video_profile_title)
            entryValues = CameraHelper.instance.requireBackDevice().videoProfiles
                .map { it.entryValue }
                .toTypedArray()
            entries = CameraHelper.instance.requireBackDevice().videoProfiles
                .map {
                    getString(R.string.profile_video_profile_entry, it.width, it.height, it.ratioWidth, it.ratioHeight,
                        it.megapixel, it.qualityString)
                }
                .toTypedArray()
            setIcon(R.drawable.profile_video_profile)
            setDialogTitle(R.string.profile_back_video_profile_title)
            setDialogIcon(R.drawable.profile_video_profile)
        }

        findPreference<SimplePreferenceGroup>(ProfileHelper.front_photo.key)?.run {
            visibleKeysOff = arrayOf(ProfileHelper.is_video.key)
            visibleKeysOn = arrayOf(ProfileHelper.is_front.key)
        }

        SimpleListPreference(preferenceScreen.context).run {
            key = ProfileHelper.front_photo_resolution.key
            setDefaultValue(ProfileHelper.front_photo_resolution.getDefaultValue())
            findPreference<PreferenceGroup>(ProfileHelper.front_photo.key)?.addPreference(this)
            setTitle(R.string.profile_front_photo_resolution_title)
            entryValues = CameraHelper.instance.requireFrontDevice().photoResolutions
                .map { it.entryValue }
                .toTypedArray()
            entries = CameraHelper.instance.requireFrontDevice().photoResolutions
                .map {
                    getString(R.string.profile_photo_resolution_entry, it.width, it.height, it.ratioWidth,
                        it.ratioHeight, it.megapixel)
                }
                .toTypedArray()
            setIcon(R.drawable.profile_resolution)
            setDialogTitle(R.string.profile_front_photo_resolution_title)
            setDialogIcon(R.drawable.profile_resolution)
        }

        findPreference<SimplePreferenceGroup>(ProfileHelper.front_video.key)?.run {
            visibleKeysOn = arrayOf(ProfileHelper.is_front.key, ProfileHelper.is_video.key)
        }

        SimpleListPreference(preferenceScreen.context).run {
            key = ProfileHelper.front_video_profile.key
            setDefaultValue(ProfileHelper.front_video_profile.getDefaultValue())
            findPreference<PreferenceGroup>(ProfileHelper.front_video.key)?.addPreference(this)
            setTitle(R.string.profile_front_video_profile_title)
            entryValues = CameraHelper.instance.requireFrontDevice().videoProfiles
                .map { it.entryValue }
                .toTypedArray()
            entries = CameraHelper.instance.requireFrontDevice().videoProfiles
                .map {
                    getString(R.string.profile_video_profile_entry, it.width, it.height, it.ratioWidth, it.ratioHeight,
                        it.megapixel, it.qualityString)
                }
                .toTypedArray()
            setIcon(R.drawable.profile_video_profile)
            setDialogTitle(R.string.profile_front_video_profile_title)
            setDialogIcon(R.drawable.profile_video_profile)
        }

        invalidateCameraState(ProfileHelper.cameraState)
    }

    override val isEventBusEnabled: Boolean = true

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CameraStateEvent) {
        invalidateCameraState(event.cameraState)
    }

    private fun invalidateCameraState(cameraState: CameraState) {
        preferenceScreen?.isEnabled = cameraState == CameraState.CLOSED ||
                cameraState == CameraState.PREVIEWING_PHOTO ||
                cameraState == CameraState.PREVIEWING_VIDEO ||
                cameraState == CameraState.CAPTURING_VIDEO
    }
}
