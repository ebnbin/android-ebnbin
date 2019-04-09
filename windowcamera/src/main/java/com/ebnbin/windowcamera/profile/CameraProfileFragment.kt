package com.ebnbin.windowcamera.profile

import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.event.CameraProfileEvent
import com.ebnbin.windowcamera.profile.preference.EmptyPreferenceGroup
import com.ebnbin.windowcamera.profile.preference.FooterPreference
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CameraProfileFragment : BaseProfileFragment() {
    private lateinit var isFrontPreference: SwitchPreferenceCompat
    private lateinit var isPreviewOnlyPreference: CheckBoxPreference
    private lateinit var isVideoPreference: SwitchPreferenceCompat
    private lateinit var backPhotoPreferenceGroup: EmptyPreferenceGroup
    private lateinit var backVideoPreferenceGroup: EmptyPreferenceGroup
    private lateinit var backPreviewPreferenceGroup: EmptyPreferenceGroup
    private lateinit var frontPhotoPreferenceGroup: EmptyPreferenceGroup
    private lateinit var frontVideoPreferenceGroup: EmptyPreferenceGroup
    private lateinit var frontPreviewPreferenceGroup: EmptyPreferenceGroup
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
                backPhotoPreferenceGroup.isVisible = !newValue &&
                        !isPreviewOnlyPreference.isChecked &&
                        !isVideoPreference.isChecked
                backVideoPreferenceGroup.isVisible = !newValue &&
                        !isPreviewOnlyPreference.isChecked &&
                        isVideoPreference.isChecked
                backPreviewPreferenceGroup.isVisible = !newValue &&
                        isPreviewOnlyPreference.isChecked
                frontPhotoPreferenceGroup.isVisible = newValue &&
                        !isPreviewOnlyPreference.isChecked &&
                        !isVideoPreference.isChecked
                frontVideoPreferenceGroup.isVisible = newValue &&
                        !isPreviewOnlyPreference.isChecked &&
                        isVideoPreference.isChecked
                frontPreviewPreferenceGroup.isVisible = newValue &&
                        isPreviewOnlyPreference.isChecked
                true
            }
            invalidateIcon(isChecked)
            preferenceScreen.addPreference(this)
        }

        isPreviewOnlyPreference = CheckBoxPreference(requireContext()).apply {
            key = ProfileHelper.KEY_IS_PREVIEW_ONLY
            setDefaultValue(ProfileHelper.DEF_VALUE_IS_PREVIEW_ONLY)
            setTitle(R.string.profile_is_preview_only)
            setSummaryOff(R.string.profile_is_preview_summary_off)
            setSummaryOn(R.string.profile_is_preview_summary_on)
            setOnPreferenceChangeListener { _, newValue ->
                newValue as Boolean
                isVideoPreference.isVisible = !newValue
                backPhotoPreferenceGroup.isVisible = !isFrontPreference.isChecked &&
                        !newValue &&
                        !isVideoPreference.isChecked
                backVideoPreferenceGroup.isVisible = !isFrontPreference.isChecked &&
                        !newValue &&
                        isVideoPreference.isChecked
                backPreviewPreferenceGroup.isVisible = !isFrontPreference.isChecked &&
                        newValue
                frontPhotoPreferenceGroup.isVisible = isFrontPreference.isChecked &&
                        !newValue &&
                        !isVideoPreference.isChecked
                frontVideoPreferenceGroup.isVisible = isFrontPreference.isChecked &&
                        !newValue &&
                        isVideoPreference.isChecked
                frontPreviewPreferenceGroup.isVisible = isFrontPreference.isChecked &&
                        newValue
                true
            }
            preferenceScreen.addPreference(this)
        }

        isVideoPreference = SwitchPreferenceCompat(requireContext()).apply {
            fun invalidateIcon(value: Boolean) {
                setIcon(if (value) R.drawable.profile_is_video_on else R.drawable.profile_is_video_off)
            }

            key = ProfileHelper.KEY_IS_VIDEO
            setDefaultValue(ProfileHelper.DEF_VALUE_IS_VIDEO)
            setTitle(R.string.profile_is_video)
            isVisible = !isPreviewOnlyPreference.isChecked
            setSummaryOff(R.string.profile_is_video_summary_off)
            setSummaryOn(R.string.profile_is_video_summary_on)
            setOnPreferenceChangeListener { _, newValue ->
                newValue as Boolean
                invalidateIcon(newValue)
                backPhotoPreferenceGroup.isVisible = !isFrontPreference.isChecked &&
                        !isPreviewOnlyPreference.isChecked &&
                        !newValue
                backVideoPreferenceGroup.isVisible = !isFrontPreference.isChecked &&
                        !isPreviewOnlyPreference.isChecked &&
                        newValue
                backPreviewPreferenceGroup.isVisible = !isFrontPreference.isChecked &&
                        isPreviewOnlyPreference.isChecked
                frontPhotoPreferenceGroup.isVisible = isFrontPreference.isChecked &&
                        !isPreviewOnlyPreference.isChecked &&
                        !newValue
                frontVideoPreferenceGroup.isVisible = isFrontPreference.isChecked &&
                        !isPreviewOnlyPreference.isChecked &&
                        newValue
                frontPreviewPreferenceGroup.isVisible = isFrontPreference.isChecked &&
                        isPreviewOnlyPreference.isChecked
                true
            }
            invalidateIcon(isChecked)
            preferenceScreen.addPreference(this)
        }

        backPhotoPreferenceGroup = EmptyPreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_BACK_PHOTO
            isVisible = !isFrontPreference.isChecked &&
                    !isPreviewOnlyPreference.isChecked &&
                    !isVideoPreference.isChecked
            preferenceScreen.addPreference(this)
            addPreference(Preference(requireContext()).apply {
                title = "Back Photo"
            })
        }

        backVideoPreferenceGroup = EmptyPreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_BACK_VIDEO
            isVisible = !isFrontPreference.isChecked &&
                    !isPreviewOnlyPreference.isChecked &&
                    isVideoPreference.isChecked
            preferenceScreen.addPreference(this)
            addPreference(Preference(requireContext()).apply {
                title = "Back Video"
            })
        }

        backPreviewPreferenceGroup = EmptyPreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_BACK_PREVIEW
            isVisible = !isFrontPreference.isChecked &&
                    isPreviewOnlyPreference.isChecked
            preferenceScreen.addPreference(this)
            addPreference(Preference(requireContext()).apply {
                title = "Back Preview"
            })
        }

        frontPhotoPreferenceGroup = EmptyPreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_FRONT_PHOTO
            isVisible = isFrontPreference.isChecked &&
                    !isPreviewOnlyPreference.isChecked &&
                    !isVideoPreference.isChecked
            preferenceScreen.addPreference(this)
            addPreference(Preference(requireContext()).apply {
                title = "Front Photo"
            })
        }

        frontVideoPreferenceGroup = EmptyPreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_FRONT_VIDEO
            isVisible = isFrontPreference.isChecked &&
                    !isPreviewOnlyPreference.isChecked &&
                    isVideoPreference.isChecked
            preferenceScreen.addPreference(this)
            addPreference(Preference(requireContext()).apply {
                title = "Front Video"
            })
        }

        frontPreviewPreferenceGroup = EmptyPreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_FRONT_PREVIEW
            isVisible = isFrontPreference.isChecked &&
                    isPreviewOnlyPreference.isChecked
            preferenceScreen.addPreference(this)
            addPreference(Preference(requireContext()).apply {
                title = "Front Preview"
            })
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
            isPreviewOnlyPreference.isEnabled = false
            isVideoPreference.isEnabled = false
            backPhotoPreferenceGroup.isEnabled = false
            backVideoPreferenceGroup.isEnabled = false
            backPreviewPreferenceGroup.isEnabled = false
            frontPhotoPreferenceGroup.isEnabled = false
            frontVideoPreferenceGroup.isEnabled = false
            frontPreviewPreferenceGroup.isEnabled = false

        } else {
            isFrontPreference.isEnabled = true
            isPreviewOnlyPreference.isEnabled = true
            isVideoPreference.isEnabled = true
            backPhotoPreferenceGroup.isEnabled = true
            backVideoPreferenceGroup.isEnabled = true
            backPreviewPreferenceGroup.isEnabled = true
            frontPhotoPreferenceGroup.isEnabled = true
            frontVideoPreferenceGroup.isEnabled = true
            frontPreviewPreferenceGroup.isEnabled = true
        }
    }
}
