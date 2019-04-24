package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceGroup
import com.ebnbin.eb.preference.FooterPreference
import com.ebnbin.eb.preference.SimplePreferenceGroup
import com.ebnbin.eb.preference.SimpleSwitchPreference
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
        SimpleSwitchPreference(preferenceScreen.context).run {
            key = ProfileSpManager.is_front.key
            preferenceScreen.addPreference(this)
            setDefaultValue(ProfileSpManager.is_front.getDefaultValue())
            setTitle(R.string.profile_is_front)
            setSummaryOff(R.string.profile_is_front_summary_off)
            setSummaryOn(R.string.profile_is_front_summary_on)
            iconOff = R.drawable.profile_is_front_off
            iconOn = R.drawable.profile_is_front_on
        }

        SimpleSwitchPreference(preferenceScreen.context).run {
            key = ProfileSpManager.is_video.key
            preferenceScreen.addPreference(this)
            setDefaultValue(ProfileSpManager.is_video.getDefaultValue())
            setTitle(R.string.profile_is_video)
            setSummaryOff(R.string.profile_is_video_summary_off)
            setSummaryOn(R.string.profile_is_video_summary_on)
            iconOff = R.drawable.profile_is_video_off
            iconOn = R.drawable.profile_is_video_on
        }

        SimplePreferenceGroup(preferenceScreen.context).run {
            key = ProfileSpManager.back_photo.key
            preferenceScreen.addPreference(this)
            visibleKeysOff = arrayOf(ProfileSpManager.is_front.key, ProfileSpManager.is_video.key)
        }

        ListPreference(preferenceScreen.context).run {
            key = ProfileSpManager.back_photo_resolution.key
            findPreference<PreferenceGroup>(ProfileSpManager.back_photo.key)?.addPreference(this)
            setDefaultValue(ProfileSpManager.back_photo_resolution.getDefaultValue())
            setTitle(R.string.profile_back_photo_resolution)
            summaryProvider = Preference.SummaryProvider<ListPreference> { entry }
            entries = CameraHelper.backDevice.photoResolutions
                .map { getString(R.string.profile_back_photo_resolution_summary, it.width, it.height, it.ratioWidth,
                    it.ratioHeight, it.megapixel) }
                .toTypedArray()
            entryValues = CameraHelper.backDevice.photoResolutions
                .map { it.key }
                .toTypedArray()
            setDialogTitle(R.string.profile_back_photo_resolution)
            setIcon(R.drawable.profile_resolution)
            setDialogIcon(R.drawable.profile_resolution)
        }

        SimplePreferenceGroup(preferenceScreen.context).run {
            key = ProfileSpManager.back_video.key
            preferenceScreen.addPreference(this)
            visibleKeysOff = arrayOf(ProfileSpManager.is_front.key)
            visibleKeysOn = arrayOf(ProfileSpManager.is_video.key)
        }

        ListPreference(preferenceScreen.context).run {
            key = ProfileSpManager.back_video_profile.key
            findPreference<PreferenceGroup>(ProfileSpManager.back_video.key)?.addPreference(this)
            setDefaultValue(ProfileSpManager.back_video_profile.getDefaultValue())
            setTitle(R.string.profile_back_video_profile)
            summaryProvider = Preference.SummaryProvider<ListPreference> { entry }
            entries = CameraHelper.backDevice.videoProfiles
                .map {
                    getString(R.string.profile_back_video_resolution_summary, it.width, it.height, it.ratioWidth,
                        it.ratioHeight, it.megapixel, it.qualityString) }
                .toTypedArray()
            entryValues = CameraHelper.backDevice.videoProfiles
                .map { it.key }
                .toTypedArray()
            setDialogTitle(R.string.profile_back_video_profile)
            setIcon(R.drawable.profile_video_profile)
            setDialogIcon(R.drawable.profile_video_profile)
        }

        SimplePreferenceGroup(preferenceScreen.context).run {
            key = ProfileSpManager.front_photo.key
            preferenceScreen.addPreference(this)
            visibleKeysOff = arrayOf(ProfileSpManager.is_video.key)
            visibleKeysOn = arrayOf(ProfileSpManager.is_front.key)
        }

        ListPreference(preferenceScreen.context).run {
            key = ProfileSpManager.front_photo_resolution.key
            findPreference<PreferenceGroup>(ProfileSpManager.front_photo.key)?.addPreference(this)
            setDefaultValue(ProfileSpManager.front_photo_resolution.getDefaultValue())
            setTitle(R.string.profile_front_photo_resolution)
            summaryProvider = Preference.SummaryProvider<ListPreference> { entry }
            entries = CameraHelper.frontDevice.photoResolutions
                .map { getString(R.string.profile_front_photo_resolution_summary, it.width, it.height, it.ratioWidth,
                    it.ratioHeight, it.megapixel) }
                .toTypedArray()
            entryValues = CameraHelper.frontDevice.photoResolutions
                .map { it.key }
                .toTypedArray()
            setDialogTitle(R.string.profile_front_photo_resolution)
            setIcon(R.drawable.profile_resolution)
            setDialogIcon(R.drawable.profile_resolution)
        }

        SimplePreferenceGroup(preferenceScreen.context).run {
            key = ProfileSpManager.front_video.key
            preferenceScreen.addPreference(this)
            visibleKeysOn = arrayOf(ProfileSpManager.is_front.key, ProfileSpManager.is_video.key)
        }

        ListPreference(preferenceScreen.context).run {
            key = ProfileSpManager.front_video_profile.key
            findPreference<PreferenceGroup>(ProfileSpManager.front_video.key)?.addPreference(this)
            setDefaultValue(ProfileSpManager.front_video_profile.getDefaultValue())
            setTitle(R.string.profile_front_video_profile)
            summaryProvider = Preference.SummaryProvider<ListPreference> { entry }
            entries = CameraHelper.frontDevice.videoProfiles
                .map {
                    getString(R.string.profile_front_video_resolution_summary, it.width, it.height, it.ratioWidth,
                        it.ratioHeight, it.megapixel, it.qualityString) }
                .toTypedArray()
            entryValues = CameraHelper.frontDevice.videoProfiles
                .map { it.key }
                .toTypedArray()
            setDialogTitle(R.string.profile_front_video_profile)
            setIcon(R.drawable.profile_video_profile)
            setDialogIcon(R.drawable.profile_video_profile)
        }

        FooterPreference(preferenceScreen.context).run {
            preferenceScreen.addPreference(this)
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
