package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceGroup
import com.ebnbin.eb.preference.EBPreferenceFragment
import com.ebnbin.eb.preference.LockablePreference
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.profile.ProfileSp

abstract class BaseProfileFragment : EBPreferenceFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName =
            SharedPreferencesHelper.getName(ProfileHelper.getSharedPreferencesNamePostfix())

        setPreferencesFromResource(R.xml.profile_fragment, rootKey)
    }

    protected fun <T> buildPreference(preference: Preference, sp: ProfileSp<T>, groupSp: ProfileSp<Unit>? = null) {
        preference.key = sp.key
        val builder = sp.builder()
        if (builder.defaultValue != Unit) {
            preference.setDefaultValue(builder.defaultValue)
        }
        val preferenceGroup = if (groupSp == null) {
            preferenceScreen
        } else {
            findPreference<PreferenceGroup>(groupSp.key) ?: throw RuntimeException()
        }
        preferenceGroup.addPreference(preference)
        builder.isVisible?.let { preference.isVisible = it }
        builder.isEnabled?.let { preference.isEnabled = it }
        if (preference is LockablePreference) {
            builder.isLockable?.let { preference.getLockDelegate().isLockable = it }
            builder.isLockedDefaultValue?.let { preference.getLockDelegate().isLockedSp.setDefaultValue(it) }
        }
    }
}
