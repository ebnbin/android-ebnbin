package com.ebnbin.eb.preference

import android.content.Context
import android.content.SharedPreferences
import com.ebnbin.eb.sharedpreferences.get

open class CheckBoxPreference(context: Context) :
    androidx.preference.CheckBoxPreference(context),
    SharedPreferences.OnSharedPreferenceChangeListener {
    init {
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
        val value = sharedPreferences.get(key, isChecked)
        if (isChecked == value) return
        isChecked = value
    }
}
