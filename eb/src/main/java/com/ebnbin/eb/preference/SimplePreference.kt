package com.ebnbin.eb.preference

import android.content.Context
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceViewHolder

open class SimplePreference(context: Context) : Preference(context),
    PreferenceLockDelegate.Callback,
    LockablePreference
{
    private var preferenceLockDelegate: PreferenceLockDelegate? = null

    override fun getLockDelegate(): PreferenceLockDelegate {
        return preferenceLockDelegate ?: throw RuntimeException()
    }

    override fun onAttachedToHierarchy(preferenceManager: PreferenceManager?) {
        super.onAttachedToHierarchy(preferenceManager)
        preferenceLockDelegate = PreferenceLockDelegate(this)
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        preferenceLockDelegate?.onBindViewHolder(holder)
    }

    override fun notifyChanged() {
        super.notifyChanged()
    }
}
