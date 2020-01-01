package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.ebnbin.eb.sharedpreferences.getOrNull
import com.ebnbin.eb.mainHandler
import com.ebnbin.eb.preference.CheckBoxPreference
import com.ebnbin.eb.preference.ListPreference
import com.ebnbin.eb.preference.PreferenceGroup
import com.ebnbin.eb.preference.SwitchPreference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.profile.CameraState
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.jeremyliao.liveeventbus.LiveEventBus

class CameraProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        SwitchPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_front)
            setTitle(R.string.profile_is_front_title)
            setSummaryOff(R.string.profile_is_front_summary_off)
            setSummaryOn(R.string.profile_is_front_summary_on)
            setIcons(R.drawable.profile_is_front_off, R.drawable.profile_is_front_on)
        }

        CheckBoxPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_preview)
            setTitle(R.string.profile_is_preview_title)
            setIcon(R.drawable.profile_is_preview)
        }

        PreferenceGroup(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_preview_off)
            setVisibleProvider(ProfileHelper.is_preview.key) {
                val sharedPreferences = it.sharedPreferences ?: return@setVisibleProvider null
                val result = !(sharedPreferences.getOrNull<Boolean>(ProfileHelper.is_preview.key)
                    ?: return@setVisibleProvider null)
                result
            }
        }

        SwitchPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_video, ProfileHelper.is_preview_off)
            setTitle(R.string.profile_is_video_title)
            setSummaryOff(R.string.profile_is_video_summary_off)
            setSummaryOn(R.string.profile_is_video_summary_on)
            setIcons(R.drawable.profile_is_video_off, R.drawable.profile_is_video_on)
        }

        PreferenceGroup(requireContext()).apply {
            buildPreference(this, ProfileHelper.back_photo, ProfileHelper.is_preview_off)
            setVisibleProvider(ProfileHelper.is_front.key, ProfileHelper.is_video.key) {
                val sharedPreferences = it.sharedPreferences ?: return@setVisibleProvider null
                var result = !(sharedPreferences.getOrNull<Boolean>(ProfileHelper.is_front.key)
                    ?: return@setVisibleProvider null)
                result = result && !(sharedPreferences.getOrNull<Boolean>(ProfileHelper.is_video.key)
                    ?: return@setVisibleProvider null)
                result
            }
        }

        ListPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.back_photo_resolution, ProfileHelper.back_photo)
            setTitle(R.string.profile_back_photo_resolution_title)
            entryValues = CameraHelper.requireBackDevice().photoResolutions
                .map { it.entryValue }
                .toTypedArray()
            entries = CameraHelper.requireBackDevice().photoResolutions
                .map {
                    getString(R.string.profile_photo_resolution_entry, it.width, it.height, it.ratio.width,
                        it.ratio.height, it.megapixel)
                }
                .toTypedArray()
            setIcon(R.drawable.profile_resolution)
            setDialogTitle(R.string.profile_back_photo_resolution_title)
            setDialogIcon(R.drawable.profile_resolution)
        }

        PreferenceGroup(requireContext()).apply {
            buildPreference(this, ProfileHelper.back_video, ProfileHelper.is_preview_off)
            setVisibleProvider(ProfileHelper.is_front.key, ProfileHelper.is_video.key) {
                val sharedPreferences = it.sharedPreferences ?: return@setVisibleProvider null
                var result = !(sharedPreferences.getOrNull<Boolean>(ProfileHelper.is_front.key)
                    ?: return@setVisibleProvider null)
                result = result && sharedPreferences.getOrNull<Boolean>(ProfileHelper.is_video.key)
                    ?: return@setVisibleProvider null
                result
            }
        }

        ListPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.back_video_profile, ProfileHelper.back_video)
            setTitle(R.string.profile_back_video_profile_title)
            entryValues = CameraHelper.requireBackDevice().videoProfiles
                .map { it.entryValue }
                .toTypedArray()
            entries = CameraHelper.requireBackDevice().videoProfiles
                .map {
                    getString(R.string.profile_video_profile_entry, it.width, it.height, it.ratio.width,
                        it.ratio.height, it.megapixel, it.qualityString)
                }
                .toTypedArray()
            setIcon(R.drawable.profile_video_profile)
            setDialogTitle(R.string.profile_back_video_profile_title)
            setDialogIcon(R.drawable.profile_video_profile)
        }

        PreferenceGroup(requireContext()).apply {
            buildPreference(this, ProfileHelper.front_photo, ProfileHelper.is_preview_off)
            setVisibleProvider(ProfileHelper.is_video.key, ProfileHelper.is_front.key) {
                val sharedPreferences = it.sharedPreferences ?: return@setVisibleProvider null
                var result = !(sharedPreferences.getOrNull<Boolean>(ProfileHelper.is_video.key)
                    ?: return@setVisibleProvider null)
                result = result && sharedPreferences.getOrNull<Boolean>(ProfileHelper.is_front.key)
                        ?: return@setVisibleProvider null
                result
            }
        }

        ListPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.front_photo_resolution, ProfileHelper.front_photo)
            setTitle(R.string.profile_front_photo_resolution_title)
            entryValues = CameraHelper.requireFrontDevice().photoResolutions
                .map { it.entryValue }
                .toTypedArray()
            entries = CameraHelper.requireFrontDevice().photoResolutions
                .map {
                    getString(R.string.profile_photo_resolution_entry, it.width, it.height, it.ratio.width,
                        it.ratio.height, it.megapixel)
                }
                .toTypedArray()
            setIcon(R.drawable.profile_resolution)
            setDialogTitle(R.string.profile_front_photo_resolution_title)
            setDialogIcon(R.drawable.profile_resolution)
        }

        PreferenceGroup(requireContext()).apply {
            buildPreference(this, ProfileHelper.front_video, ProfileHelper.is_preview_off)
            setVisibleProvider(ProfileHelper.is_front.key, ProfileHelper.is_video.key) {
                val sharedPreferences = it.sharedPreferences ?: return@setVisibleProvider null
                var result = sharedPreferences.getOrNull<Boolean>(ProfileHelper.is_front.key)
                    ?: return@setVisibleProvider null
                result = result && sharedPreferences.getOrNull<Boolean>(ProfileHelper.is_video.key)
                        ?: return@setVisibleProvider null
                result
            }
        }

        ListPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.front_video_profile, ProfileHelper.front_video)
            setTitle(R.string.profile_front_video_profile_title)
            entryValues = CameraHelper.requireFrontDevice().videoProfiles
                .map { it.entryValue }
                .toTypedArray()
            entries = CameraHelper.requireFrontDevice().videoProfiles
                .map {
                    getString(R.string.profile_video_profile_entry, it.width, it.height, it.ratio.width,
                        it.ratio.height, it.megapixel, it.qualityString)
                }
                .toTypedArray()
            setIcon(R.drawable.profile_video_profile)
            setDialogTitle(R.string.profile_front_video_profile_title)
            setDialogIcon(R.drawable.profile_video_profile)
        }

        mainHandler.post {
            LiveEventBus.get("CameraStateEvent").post(Unit)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LiveEventBus.get("CameraStateEvent").observe(viewLifecycleOwner, Observer {
            preferenceScreen?.isEnabled = ProfileHelper.cameraState != CameraState.STATING
        })
    }
}
