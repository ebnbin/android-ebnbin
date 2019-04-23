package com.ebnbin.windowcamera.preference

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import androidx.preference.R
import androidx.preference.SeekBarPreference
import com.ebnbin.eb.sharedpreferences.get

/**
 * 当偏好的值发生变化时自动更新.
 */
open class SimpleSeekBarPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.seekBarPreferenceStyle,
    defStyleRes: Int = 0
) : SeekBarPreference(context, attrs, defStyleAttr, defStyleRes), SharedPreferences.OnSharedPreferenceChangeListener {
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
}
