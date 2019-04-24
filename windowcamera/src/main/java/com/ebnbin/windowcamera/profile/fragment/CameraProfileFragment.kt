package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.preference.FooterPreference
import com.ebnbin.windowcamera.preference.SimplePreferenceGroup
import com.ebnbin.windowcamera.preference.SimpleSwitchPreference
import com.ebnbin.windowcamera.profile.CameraProfileEvent
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.profile.ProfileSpManager
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CameraProfileFragment : BaseProfileFragment() {
    private lateinit var isFrontPreference: SimpleSwitchPreference
    private lateinit var isVideoPreference: SimpleSwitchPreference
    private lateinit var backPhotoPreferenceGroup: SimplePreferenceGroup
    private lateinit var backPhotoResolutionPreference: ListPreference
    private lateinit var backVideoPreferenceGroup: SimplePreferenceGroup
    private lateinit var backVideoProfilePreference: ListPreference
    private lateinit var frontPhotoPreferenceGroup: SimplePreferenceGroup
    private lateinit var frontPhotoResolutionPreference: ListPreference
    private lateinit var frontVideoPreferenceGroup: SimplePreferenceGroup
    private lateinit var frontVideoProfilePreference: ListPreference
    private lateinit var footerPreference: FooterPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        isFrontPreference = SimpleSwitchPreference(requireContext()).apply {
            key = ProfileSpManager.is_front.key
            setDefaultValue(ProfileSpManager.is_front.getDefaultValue())
            setTitle(R.string.profile_is_front)
            setSummaryOff(R.string.profile_is_front_summary_off)
            setSummaryOn(R.string.profile_is_front_summary_on)
            icons = Pair(R.drawable.profile_is_front_off, R.drawable.profile_is_front_on)
            preferenceScreen.addPreference(this)
        }

        isVideoPreference = SimpleSwitchPreference(requireContext()).apply {
            key = ProfileSpManager.is_video.key
            setDefaultValue(ProfileSpManager.is_video.getDefaultValue())
            setTitle(R.string.profile_is_video)
            setSummaryOff(R.string.profile_is_video_summary_off)
            setSummaryOn(R.string.profile_is_video_summary_on)
            icons = Pair(R.drawable.profile_is_video_off, R.drawable.profile_is_video_on)
            preferenceScreen.addPreference(this)
        }

        backPhotoPreferenceGroup = SimplePreferenceGroup(requireContext()).apply {
            key = ProfileSpManager.back_photo.key
            visibleTwoStatePreferences = Pair(setOf(isFrontPreference, isVideoPreference), null)
            preferenceScreen.addPreference(this)
        }

        backPhotoResolutionPreference = ListPreference(requireContext()).apply {
            key = ProfileSpManager.back_photo_resolution.key
            setDefaultValue(ProfileSpManager.back_photo_resolution.getDefaultValue())
            setTitle(R.string.profile_back_photo_resolution)
            summaryProvider = Preference.SummaryProvider<ListPreference> {
                val resolution = CameraHelper.backDevice.getPhotoResolutionOrElse(it.value) {
                    val result = CameraHelper.backDevice.photoResolutions.first()
                    value = result.key
                    result
                }
                getString(R.string.profile_back_photo_resolution_summary, resolution.width, resolution.height,
                    resolution.ratioWidth, resolution.ratioHeight, resolution.megapixel)
            }
            entries = CameraHelper.backDevice.photoResolutions
                .map { getString(R.string.profile_back_photo_resolution_summary, it.width, it.height, it.ratioWidth,
                    it.ratioHeight, it.megapixel) }
                .toTypedArray()
            entryValues = CameraHelper.backDevice.photoResolutions
                .map { it.key }
                .toTypedArray()
            setDialogTitle(R.string.profile_back_photo_resolution)
            backPhotoPreferenceGroup.addPreference(this)
        }

        backVideoPreferenceGroup = SimplePreferenceGroup(requireContext()).apply {
            key = ProfileSpManager.back_video.key
            visibleTwoStatePreferences = Pair(setOf(isFrontPreference), setOf(isVideoPreference))
            preferenceScreen.addPreference(this)
        }

        backVideoProfilePreference = ListPreference(requireContext()).apply {
            key = ProfileSpManager.back_video_profile.key
            setDefaultValue(ProfileSpManager.back_video_profile.getDefaultValue())
            setTitle(R.string.profile_back_video_profile)
            summaryProvider = Preference.SummaryProvider<ListPreference> {
                val videoProfile = CameraHelper.backDevice.getVideoProfileOrElse(it.value) {
                    val result = CameraHelper.backDevice.videoProfiles.first()
                    value = result.key
                    result
                }
                getString(R.string.profile_back_video_resolution_summary, videoProfile.width, videoProfile.height,
                    videoProfile.ratioWidth, videoProfile.ratioHeight, videoProfile.megapixel,
                    videoProfile.qualityString)
            }
            entries = CameraHelper.backDevice.videoProfiles
                .map {
                    getString(R.string.profile_back_video_resolution_summary, it.width, it.height, it.ratioWidth,
                        it.ratioHeight, it.megapixel, it.qualityString) }
                .toTypedArray()
            entryValues = CameraHelper.backDevice.videoProfiles
                .map { it.key }
                .toTypedArray()
            setDialogTitle(R.string.profile_back_video_profile)
            backVideoPreferenceGroup.addPreference(this)
        }

        frontPhotoPreferenceGroup = SimplePreferenceGroup(requireContext()).apply {
            key = ProfileSpManager.front_photo.key
            visibleTwoStatePreferences = Pair(setOf(isVideoPreference), setOf(isFrontPreference))
            preferenceScreen.addPreference(this)
        }

        frontPhotoResolutionPreference = ListPreference(requireContext()).apply {
            key = ProfileSpManager.front_photo_resolution.key
            setDefaultValue(ProfileSpManager.front_photo_resolution.getDefaultValue())
            setTitle(R.string.profile_front_photo_resolution)
            summaryProvider = Preference.SummaryProvider<ListPreference> {
                val resolution = CameraHelper.frontDevice.getPhotoResolutionOrElse(it.value) {
                    val result = CameraHelper.frontDevice.photoResolutions.first()
                    value = result.key
                    result
                }
                getString(R.string.profile_front_photo_resolution_summary, resolution.width, resolution.height,
                    resolution.ratioWidth, resolution.ratioHeight, resolution.megapixel)
            }
            entries = CameraHelper.frontDevice.photoResolutions
                .map { getString(R.string.profile_front_photo_resolution_summary, it.width, it.height, it.ratioWidth,
                    it.ratioHeight, it.megapixel) }
                .toTypedArray()
            entryValues = CameraHelper.frontDevice.photoResolutions
                .map { it.key }
                .toTypedArray()
            setDialogTitle(R.string.profile_front_photo_resolution)
            frontPhotoPreferenceGroup.addPreference(this)
        }

        frontVideoPreferenceGroup = SimplePreferenceGroup(requireContext()).apply {
            key = ProfileSpManager.front_video.key
            visibleTwoStatePreferences = Pair(null, setOf(isFrontPreference, isVideoPreference))
            preferenceScreen.addPreference(this)
        }

        frontVideoProfilePreference = ListPreference(requireContext()).apply {
            key = ProfileSpManager.front_video_profile.key
            setDefaultValue(ProfileSpManager.front_video_profile.getDefaultValue())
            setTitle(R.string.profile_front_video_profile)
            summaryProvider = Preference.SummaryProvider<ListPreference> {
                val videoProfile = CameraHelper.frontDevice.getVideoProfileOrElse(it.value) {
                    val result = CameraHelper.frontDevice.videoProfiles.first()
                    value = result.key
                    result
                }
                getString(R.string.profile_front_video_resolution_summary, videoProfile.width, videoProfile.height,
                    videoProfile.ratioWidth, videoProfile.ratioHeight, videoProfile.megapixel,
                    videoProfile.qualityString)
            }
            entries = CameraHelper.frontDevice.videoProfiles
                .map {
                    getString(R.string.profile_front_video_resolution_summary, it.width, it.height, it.ratioWidth,
                        it.ratioHeight, it.megapixel, it.qualityString) }
                .toTypedArray()
            entryValues = CameraHelper.frontDevice.videoProfiles
                .map { it.key }
                .toTypedArray()
            setDialogTitle(R.string.profile_front_video_profile)
            frontVideoPreferenceGroup.addPreference(this)
        }

        footerPreference = FooterPreference(requireContext()).apply {
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
        if (isInvalidating) {
            isFrontPreference.isEnabled = false
            isVideoPreference.isEnabled = false
            backPhotoPreferenceGroup.isEnabled = false
            backVideoPreferenceGroup.isEnabled = false
            frontPhotoPreferenceGroup.isEnabled = false
            frontVideoPreferenceGroup.isEnabled = false

        } else {
            isFrontPreference.isEnabled = true
            isVideoPreference.isEnabled = true
            backPhotoPreferenceGroup.isEnabled = true
            backVideoPreferenceGroup.isEnabled = true
            frontPhotoPreferenceGroup.isEnabled = true
            frontVideoPreferenceGroup.isEnabled = true
        }
    }
}
