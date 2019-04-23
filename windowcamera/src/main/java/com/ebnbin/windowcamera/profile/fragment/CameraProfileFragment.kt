package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.preference.FooterPreference
import com.ebnbin.windowcamera.preference.SimplePreferenceGroup
import com.ebnbin.windowcamera.profile.CameraProfileEvent
import com.ebnbin.windowcamera.profile.ProfileHelper
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CameraProfileFragment : BaseProfileFragment() {
    private lateinit var isFrontPreference: SwitchPreferenceCompat
    private lateinit var isVideoPreference: SwitchPreferenceCompat
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
        isFrontPreference = SwitchPreferenceCompat(requireContext()).apply {
            fun invalidateIcon(value: Boolean) {
                setIcon(if (value) R.drawable.profile_is_front_on else R.drawable.profile_is_front_off)
            }

            key = ProfileHelper.KEY_IS_FRONT
            setDefaultValue(ProfileHelper.DEF_VALUE_IS_FRONT)
            setTitle(R.string.profile_is_front)
            setSummaryOff(R.string.profile_is_front_summary_off)
            setSummaryOn(R.string.profile_is_front_summary_on)
            setOnPreferenceChangeListener { _, newValue ->
                newValue as Boolean
                invalidateIcon(newValue)
                backPhotoPreferenceGroup.isVisible = !newValue && !isVideoPreference.isChecked
                backVideoPreferenceGroup.isVisible = !newValue && isVideoPreference.isChecked
                frontPhotoPreferenceGroup.isVisible = newValue && !isVideoPreference.isChecked
                frontVideoPreferenceGroup.isVisible = newValue && isVideoPreference.isChecked
                true
            }
            invalidateIcon(ProfileHelper.isFront)
            preferenceScreen.addPreference(this)
        }

        isVideoPreference = SwitchPreferenceCompat(requireContext()).apply {
            fun invalidateIcon(value: Boolean) {
                setIcon(if (value) R.drawable.profile_is_video_on else R.drawable.profile_is_video_off)
            }

            key = ProfileHelper.KEY_IS_VIDEO
            setDefaultValue(ProfileHelper.DEF_VALUE_IS_VIDEO)
            setTitle(R.string.profile_is_video)
            setSummaryOff(R.string.profile_is_video_summary_off)
            setSummaryOn(R.string.profile_is_video_summary_on)
            setOnPreferenceChangeListener { _, newValue ->
                newValue as Boolean
                invalidateIcon(newValue)
                backPhotoPreferenceGroup.isVisible = !isFrontPreference.isChecked && !newValue
                backVideoPreferenceGroup.isVisible = !isFrontPreference.isChecked && newValue
                frontPhotoPreferenceGroup.isVisible = isFrontPreference.isChecked && !newValue
                frontVideoPreferenceGroup.isVisible = isFrontPreference.isChecked && newValue
                true
            }
            invalidateIcon(ProfileHelper.isVideo)
            preferenceScreen.addPreference(this)
        }

        backPhotoPreferenceGroup = SimplePreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_BACK_PHOTO
            isVisible = !isFrontPreference.isChecked && !isVideoPreference.isChecked
            preferenceScreen.addPreference(this)
        }

        backPhotoResolutionPreference = ListPreference(requireContext()).apply {
            key = ProfileHelper.KEY_BACK_PHOTO_RESOLUTION
            setDefaultValue(ProfileHelper.DEF_VALUE_BACK_PHOTO_RESOLUTION)
            setTitle(R.string.profile_back_photo_resolution)
            summaryProvider = Preference.SummaryProvider<ListPreference> {
                val resolution = CameraHelper.backDevice.getPhotoResolutionOrElse(it.value) {
                    val result = CameraHelper.backDevice.photoResolutions.first()
                    value = result.key
                    result
                }
                resolution.toString()
            }
            entries = CameraHelper.backDevice.photoResolutions
                .map { it.toString() }
                .toTypedArray()
            entryValues = CameraHelper.backDevice.photoResolutions
                .map { it.key }
                .toTypedArray()
            setDialogTitle(R.string.profile_back_photo_resolution)
            backPhotoPreferenceGroup.addPreference(this)
        }

        backVideoPreferenceGroup = SimplePreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_BACK_VIDEO
            isVisible = !isFrontPreference.isChecked && isVideoPreference.isChecked
            preferenceScreen.addPreference(this)
        }

        backVideoProfilePreference = ListPreference(requireContext()).apply {
            key = ProfileHelper.KEY_BACK_VIDEO_PROFILE
            setDefaultValue(ProfileHelper.DEF_VALUE_BACK_VIDEO_PROFILE)
            setTitle(R.string.profile_back_video_profile)
            summaryProvider = Preference.SummaryProvider<ListPreference> {
                val videoProfile = CameraHelper.backDevice.getVideoProfileOrElse(it.value) {
                    val result = CameraHelper.backDevice.videoProfiles.first()
                    value = result.key
                    result
                }
                videoProfile.toString()
            }
            entries = CameraHelper.backDevice.videoProfiles
                .map { it.toString() }
                .toTypedArray()
            entryValues = CameraHelper.backDevice.videoProfiles
                .map { it.key }
                .toTypedArray()
            setDialogTitle(R.string.profile_back_video_profile)
            backVideoPreferenceGroup.addPreference(this)
        }

        frontPhotoPreferenceGroup = SimplePreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_FRONT_PHOTO
            isVisible = isFrontPreference.isChecked && !isVideoPreference.isChecked
            preferenceScreen.addPreference(this)
        }

        frontPhotoResolutionPreference = ListPreference(requireContext()).apply {
            key = ProfileHelper.KEY_FRONT_PHOTO_RESOLUTION
            setDefaultValue(ProfileHelper.DEF_VALUE_FRONT_PHOTO_RESOLUTION)
            setTitle(R.string.profile_front_photo_resolution)
            summaryProvider = Preference.SummaryProvider<ListPreference> {
                val resolution = CameraHelper.frontDevice.getPhotoResolutionOrElse(it.value) {
                    val result = CameraHelper.frontDevice.photoResolutions.first()
                    value = result.key
                    result
                }
                resolution.toString()
            }
            entries = CameraHelper.frontDevice.photoResolutions
                .map { it.toString() }
                .toTypedArray()
            entryValues = CameraHelper.frontDevice.photoResolutions
                .map { it.key }
                .toTypedArray()
            setDialogTitle(R.string.profile_front_photo_resolution)
            frontPhotoPreferenceGroup.addPreference(this)
        }

        frontVideoPreferenceGroup = SimplePreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_FRONT_VIDEO
            isVisible = isFrontPreference.isChecked && isVideoPreference.isChecked
            preferenceScreen.addPreference(this)
        }

        frontVideoProfilePreference = ListPreference(requireContext()).apply {
            key = ProfileHelper.KEY_FRONT_VIDEO_PROFILE
            setDefaultValue(ProfileHelper.DEF_VALUE_FRONT_VIDEO_PROFILE)
            setTitle(R.string.profile_front_video_profile)
            summaryProvider = Preference.SummaryProvider<ListPreference> {
                val videoProfile = CameraHelper.frontDevice.getVideoProfileOrElse(it.value) {
                    val result = CameraHelper.frontDevice.videoProfiles.first()
                    value = result.key
                    result
                }
                videoProfile.toString()
            }
            entries = CameraHelper.frontDevice.videoProfiles
                .map { it.toString() }
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