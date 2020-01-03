package com.ebnbin.eb.preference

import android.content.Context
import android.content.SharedPreferences
import com.ebnbin.eb.sharedpreferences.get

open class SeekBarPreference(context: Context) :
    androidx.preference.SeekBarPreference(context),
    SharedPreferences.OnSharedPreferenceChangeListener {
    init {
        isSingleLineTitle = false
        showSeekBarValue = true
        updatesContinuously = true
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
        if (this.sharedPreferences != sharedPreferences || this.key != key) return
        val value = sharedPreferences.get(key, value)
        if (this.value == value) return
        this.value = value
    }
}
