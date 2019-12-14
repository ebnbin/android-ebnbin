package com.ebnbin.eb.preference

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreferenceCompat
import com.ebnbin.windowcamera.R
import com.ebnbin.eb.sharedpreferences.get

/**
 * 拥有 off/on 图标的 SwitchPreference.
 */
open class SimpleSwitchPreference(context: Context) :
    SwitchPreferenceCompat(context, null, R.attr.switchPreferenceCompatStyle),
    SharedPreferences.OnSharedPreferenceChangeListener,
    PreferenceLockDelegate.Callback,
    LockablePreference
{
    var iconOff: Int = 0
        set(value) {
            if (field == value) return
            field = value
            invalidateIcons()
        }

    var iconOn: Int = 0
        set(value) {
            if (field == value) return
            field = value
            invalidateIcons()
        }

    @Deprecated("使用 icons 代替.", ReplaceWith(""))
    override fun setIcon(icon: Drawable?) {
        super.setIcon(icon)
    }

    @Deprecated("使用 icons 代替.", ReplaceWith(""))
    override fun setIcon(iconResId: Int) {
        super.setIcon(iconResId)
    }

    private fun invalidateIcons() {
        if (isChecked) {
            if (iconOn == 0) {
                super.setIcon(null)
            } else {
                super.setIcon(iconOn)
            }
        } else {
            if (iconOff == 0) {
                super.setIcon(null)
            } else {
                super.setIcon(iconOff)
            }
        }
    }

    override fun onAttached() {
        super.onAttached()
        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        invalidateIcons()
    }

    override fun onDetached() {
        sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        super.onDetached()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        sharedPreferences ?: return
        key ?: return
        if (this.key != key) return
        invalidateIcons()
        val newValue = sharedPreferences.get(key, isChecked)
        if (isChecked == newValue) return
        isChecked = newValue
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
