package com.ebnbin.windowcamera.profile

import android.os.Bundle
import androidx.preference.SwitchPreferenceCompat
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.event.CameraProfileEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CameraProfileFragment : BaseProfileFragment() {
    private lateinit var isFrontPreference: SwitchPreferenceCompat

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
                true
            }
            invalidateIcon(isChecked)
        }
        preferenceScreen.addPreference(isFrontPreference)

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
        } else {
            isFrontPreference.isEnabled = true
        }
    }
}
