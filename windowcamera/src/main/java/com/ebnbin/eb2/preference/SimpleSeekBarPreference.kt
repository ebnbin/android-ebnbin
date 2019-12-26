package com.ebnbin.eb2.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceViewHolder
import androidx.preference.SeekBarPreference
import com.ebnbin.eb.get
import com.ebnbin.windowcamera.R

/**
 * 当偏好的值发生变化时自动更新.
 */
open class SimpleSeekBarPreference(context: Context) : SeekBarPreference(context, null, R.attr.seekBarPreferenceStyle),
    SharedPreferences.OnSharedPreferenceChangeListener,
    PreferenceLockDelegate.Callback,
    LockablePreference
{
    init {
        showSeekBarValue = true
        updatesContinuously = true
        isSingleLineTitle = false
    }

    override fun onAttached() {
        super.onAttached()
        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDetached() {
        sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        super.onDetached()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        sharedPreferences ?: return
        key ?: return
        if (this.key != key) return
        val newValue = sharedPreferences.get(key, value)
        if (value == newValue) return
        value = newValue
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
