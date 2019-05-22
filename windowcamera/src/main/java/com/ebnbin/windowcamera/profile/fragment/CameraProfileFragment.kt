package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import com.ebnbin.eb.preference.FooterPreference
import com.ebnbin.eb.preference.SimpleCheckBoxPreference
import com.ebnbin.eb.preference.SimpleListPreference
import com.ebnbin.eb.preference.SimplePreferenceGroup
import com.ebnbin.eb.preference.SimpleSwitchPreference
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
        SimpleSwitchPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.is_front)
            setTitle(R.string.profile_is_front_title)
            setSummaryOff(R.string.profile_is_front_summary_off)
            setSummaryOn(R.string.profile_is_front_summary_on)
            iconOff = R.drawable.profile_is_front_off
            iconOn = R.drawable.profile_is_front_on
        }

        SimpleCheckBoxPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.is_preview)
            setTitle(R.string.profile_is_preview_title)
            setIcon(R.drawable.profile_is_preview)
        }

        SimplePreferenceGroup(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.is_preview_off)
            visibleKeysOff = arrayOf(ProfileHelper.is_preview.key)
        }

        SimpleSwitchPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.is_video, ProfileHelper.is_preview_off)
            setTitle(R.string.profile_is_video_title)
            setSummaryOff(R.string.profile_is_video_summary_off)
            setSummaryOn(R.string.profile_is_video_summary_on)
            iconOff = R.drawable.profile_is_video_off
            iconOn = R.drawable.profile_is_video_on
        }

        SimplePreferenceGroup(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.back_photo, ProfileHelper.is_preview_off)
            visibleKeysOff = arrayOf(ProfileHelper.is_front.key, ProfileHelper.is_video.key)
        }

        SimpleListPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.back_photo_resolution, ProfileHelper.back_photo)
            setTitle(R.string.profile_back_photo_resolution_title)
            entryValues = CameraHelper.getInstance().requireBackDevice().photoResolutions
                .map { it.entryValue }
                .toTypedArray()
            entries = CameraHelper.getInstance().requireBackDevice().photoResolutions
                .map {
                    getString(R.string.profile_photo_resolution_entry, it.width, it.height, it.ratio.width,
                        it.ratio.height, it.megapixel)
                }
                .toTypedArray()
            setIcon(R.drawable.profile_resolution)
            setDialogTitle(R.string.profile_back_photo_resolution_title)
            setDialogIcon(R.drawable.profile_resolution)
        }

        SimplePreferenceGroup(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.back_video, ProfileHelper.is_preview_off)
            visibleKeysOff = arrayOf(ProfileHelper.is_front.key)
            visibleKeysOn = arrayOf(ProfileHelper.is_video.key)
        }

        SimpleListPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.back_video_profile, ProfileHelper.back_video)
            setTitle(R.string.profile_back_video_profile_title)
            entryValues = CameraHelper.getInstance().requireBackDevice().videoProfiles
                .map { it.entryValue }
                .toTypedArray()
            entries = CameraHelper.getInstance().requireBackDevice().videoProfiles
                .map {
                    getString(R.string.profile_video_profile_entry, it.width, it.height, it.ratio.width,
                        it.ratio.height, it.megapixel, it.qualityString)
                }
                .toTypedArray()
            setIcon(R.drawable.profile_video_profile)
            setDialogTitle(R.string.profile_back_video_profile_title)
            setDialogIcon(R.drawable.profile_video_profile)
        }

        SimplePreferenceGroup(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.front_photo, ProfileHelper.is_preview_off)
            visibleKeysOff = arrayOf(ProfileHelper.is_video.key)
            visibleKeysOn = arrayOf(ProfileHelper.is_front.key)
        }

        SimpleListPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.front_photo_resolution, ProfileHelper.front_photo)
            setTitle(R.string.profile_front_photo_resolution_title)
            entryValues = CameraHelper.getInstance().requireFrontDevice().photoResolutions
                .map { it.entryValue }
                .toTypedArray()
            entries = CameraHelper.getInstance().requireFrontDevice().photoResolutions
                .map {
                    getString(R.string.profile_photo_resolution_entry, it.width, it.height, it.ratio.width,
                        it.ratio.height, it.megapixel)
                }
                .toTypedArray()
            setIcon(R.drawable.profile_resolution)
            setDialogTitle(R.string.profile_front_photo_resolution_title)
            setDialogIcon(R.drawable.profile_resolution)
        }

        SimplePreferenceGroup(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.front_video, ProfileHelper.is_preview_off)
            visibleKeysOn = arrayOf(ProfileHelper.is_front.key, ProfileHelper.is_video.key)
        }

        SimpleListPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.front_video_profile, ProfileHelper.front_video)
            setTitle(R.string.profile_front_video_profile_title)
            entryValues = CameraHelper.getInstance().requireFrontDevice().videoProfiles
                .map { it.entryValue }
                .toTypedArray()
            entries = CameraHelper.getInstance().requireFrontDevice().videoProfiles
                .map {
                    getString(R.string.profile_video_profile_entry, it.width, it.height, it.ratio.width,
                        it.ratio.height, it.megapixel, it.qualityString)
                }
                .toTypedArray()
            setIcon(R.drawable.profile_video_profile)
            setDialogTitle(R.string.profile_front_video_profile_title)
            setDialogIcon(R.drawable.profile_video_profile)
        }

        FooterPreference(preferenceScreen.context).apply {
            preferenceScreen.addPreference(this)
        }

        onEvent(CameraStateEvent)
    }

    override val isEventBusEnabled: Boolean = true

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CameraStateEvent) {
        preferenceScreen?.isEnabled = ProfileHelper.cameraState != CameraState.STATING
    }
}
