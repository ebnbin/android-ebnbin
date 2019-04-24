package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceGroup
import com.ebnbin.eb.preference.SimplePreferenceGroup
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.profile.CameraProfileEvent
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.profile.ProfileSpManager
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CameraProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.profile_camera_fragment, rootKey)

        findPreference<SimplePreferenceGroup>(ProfileSpManager.back_photo.key)?.run {
            visibleKeysOff = arrayOf(ProfileSpManager.is_front.key, ProfileSpManager.is_video.key)
        }

        ListPreference(preferenceScreen.context).run {
            key = ProfileSpManager.back_photo_resolution.key
            setDefaultValue(ProfileSpManager.back_photo_resolution.getDefaultValue())
            findPreference<PreferenceGroup>(ProfileSpManager.back_photo.key)?.addPreference(this)
            setTitle(R.string.profile_back_photo_resolution_title)
            summaryProvider = Preference.SummaryProvider<ListPreference> { entry }
            entryValues = CameraHelper.backDevice.photoResolutions
                .map { it.key }
                .toTypedArray()
            entries = CameraHelper.backDevice.photoResolutions
                .map { getString(R.string.profile_photo_resolution_summary, it.width, it.height, it.ratioWidth,
                    it.ratioHeight, it.megapixel) }
                .toTypedArray()
            setIcon(R.drawable.profile_resolution)
            setDialogTitle(R.string.profile_back_photo_resolution_title)
            setDialogIcon(R.drawable.profile_resolution)
        }

        findPreference<SimplePreferenceGroup>(ProfileSpManager.back_video.key)?.run {
            visibleKeysOff = arrayOf(ProfileSpManager.is_front.key)
            visibleKeysOn = arrayOf(ProfileSpManager.is_video.key)
        }

        ListPreference(preferenceScreen.context).run {
            key = ProfileSpManager.back_video_profile.key
            setDefaultValue(ProfileSpManager.back_video_profile.getDefaultValue())
            findPreference<PreferenceGroup>(ProfileSpManager.back_video.key)?.addPreference(this)
            setTitle(R.string.profile_back_video_profile_title)
            summaryProvider = Preference.SummaryProvider<ListPreference> { entry }
            entryValues = CameraHelper.backDevice.videoProfiles
                .map { it.key }
                .toTypedArray()
            entries = CameraHelper.backDevice.videoProfiles
                .map {
                    getString(R.string.profile_video_resolution_summary, it.width, it.height, it.ratioWidth,
                        it.ratioHeight, it.megapixel, it.qualityString) }
                .toTypedArray()
            setIcon(R.drawable.profile_video_profile)
            setDialogTitle(R.string.profile_back_video_profile_title)
            setDialogIcon(R.drawable.profile_video_profile)
        }

        findPreference<SimplePreferenceGroup>(ProfileSpManager.front_photo.key)?.run {
            visibleKeysOff = arrayOf(ProfileSpManager.is_video.key)
            visibleKeysOn = arrayOf(ProfileSpManager.is_front.key)
        }

        ListPreference(preferenceScreen.context).run {
            key = ProfileSpManager.front_photo_resolution.key
            setDefaultValue(ProfileSpManager.front_photo_resolution.getDefaultValue())
            findPreference<PreferenceGroup>(ProfileSpManager.front_photo.key)?.addPreference(this)
            setTitle(R.string.profile_front_photo_resolution_title)
            summaryProvider = Preference.SummaryProvider<ListPreference> { entry }
            entryValues = CameraHelper.frontDevice.photoResolutions
                .map { it.key }
                .toTypedArray()
            entries = CameraHelper.frontDevice.photoResolutions
                .map { getString(R.string.profile_photo_resolution_summary, it.width, it.height, it.ratioWidth,
                    it.ratioHeight, it.megapixel) }
                .toTypedArray()
            setIcon(R.drawable.profile_resolution)
            setDialogTitle(R.string.profile_front_photo_resolution_title)
            setDialogIcon(R.drawable.profile_resolution)
        }

        findPreference<SimplePreferenceGroup>(ProfileSpManager.front_video.key)?.run {
            visibleKeysOn = arrayOf(ProfileSpManager.is_front.key, ProfileSpManager.is_video.key)
        }

        ListPreference(preferenceScreen.context).run {
            key = ProfileSpManager.front_video_profile.key
            setDefaultValue(ProfileSpManager.front_video_profile.getDefaultValue())
            findPreference<PreferenceGroup>(ProfileSpManager.front_video.key)?.addPreference(this)
            setTitle(R.string.profile_front_video_profile_title)
            summaryProvider = Preference.SummaryProvider<ListPreference> { entry }
            entryValues = CameraHelper.frontDevice.videoProfiles
                .map { it.key }
                .toTypedArray()
            entries = CameraHelper.frontDevice.videoProfiles
                .map {
                    getString(R.string.profile_video_resolution_summary, it.width, it.height, it.ratioWidth,
                        it.ratioHeight, it.megapixel, it.qualityString) }
                .toTypedArray()
            setIcon(R.drawable.profile_video_profile)
            setDialogTitle(R.string.profile_front_video_profile_title)
            setDialogIcon(R.drawable.profile_video_profile)
        }

        invalidateCameraProfile(ProfileHelper.isCameraProfileInvalidating)
    }

    override val isEventBusEnabled: Boolean = true

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CameraProfileEvent) {
        invalidateCameraProfile(event.isInvalidating)
    }

    private fun invalidateCameraProfile(isInvalidating: Boolean) {
        preferenceScreen.isEnabled = !isInvalidating
    }
}
