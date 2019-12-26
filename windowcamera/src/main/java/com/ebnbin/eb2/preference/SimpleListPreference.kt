package com.ebnbin.eb2.preference

import android.content.Context
import androidx.preference.ListPreference
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceViewHolder

open class SimpleListPreference(context: Context) : ListPreference(context),
    PreferenceLockDelegate.Callback,
    LockablePreference
{
    init {
        setSummaryProvider { entry }
        isSingleLineTitle = false
    }

    //*****************************************************************************************************************

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
