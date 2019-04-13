package com.ebnbin.windowcamera.profile

import android.os.Bundle
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
    private lateinit var isVideoPreference: SwitchPreferenceCompat
    private lateinit var backPhotoPreferenceGroup: EmptyPreferenceGroup
    private lateinit var backVideoPreferenceGroup: EmptyPreferenceGroup
    private lateinit var frontPhotoPreferenceGroup: EmptyPreferenceGroup
    private lateinit var frontVideoPreferenceGroup: EmptyPreferenceGroup
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
            invalidateIcon(isChecked)
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
            invalidateIcon(isChecked)
            preferenceScreen.addPreference(this)
        }

        backPhotoPreferenceGroup = EmptyPreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_BACK_PHOTO
            isVisible = !isFrontPreference.isChecked && !isVideoPreference.isChecked
            preferenceScreen.addPreference(this)
            addPreference(Preference(requireContext()).apply {
                title = "Back Photo"
            })
        }

        backVideoPreferenceGroup = EmptyPreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_BACK_VIDEO
            isVisible = !isFrontPreference.isChecked && isVideoPreference.isChecked
            preferenceScreen.addPreference(this)
            addPreference(Preference(requireContext()).apply {
                title = "Back Video"
            })
        }

        frontPhotoPreferenceGroup = EmptyPreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_FRONT_PHOTO
            isVisible = isFrontPreference.isChecked && !isVideoPreference.isChecked
            preferenceScreen.addPreference(this)
            addPreference(Preference(requireContext()).apply {
                title = "Front Photo"
            })
        }

        frontVideoPreferenceGroup = EmptyPreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_FRONT_VIDEO
            isVisible = isFrontPreference.isChecked && isVideoPreference.isChecked
            preferenceScreen.addPreference(this)
            addPreference(Preference(requireContext()).apply {
                title = "Front Video"
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
