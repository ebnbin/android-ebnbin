package com.ebnbin.eb.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceGroup
import com.ebnbin.eb.R
import com.ebnbin.eb.sharedpreferences.get

/**
 * 空 PreferenceGroup. 用于添加并管理其他 Preference.
 * 当 visibleKeysOff 中的所有 key 对应 value 都为 false 且 visibleKeysOn 中的所有 key 对应 value 都为 true 时 Preference
 * 才可见.
 */
open class SimplePreferenceGroup(context: Context) :
    PreferenceGroup(context, null, R.attr.ebSimplePreferenceGroupStyle),
    SharedPreferences.OnSharedPreferenceChangeListener
{
    var visibleKeysOff: Array<out CharSequence>? = null
        set(value) {
            if (field === value) return
            field = value
            invalidateVisible()
        }

    var visibleKeysOn: Array<out CharSequence>? = null
        set(value) {
            if (field === value) return
            field = value
            invalidateVisible()
        }

    private fun invalidateVisible() {
        isVisible = visibleKeysOff?.all { sharedPreferences?.get(it.toString(), false) != true } != false &&
                visibleKeysOn?.all { sharedPreferences?.get(it.toString(), true) != false } != false
    }

    override fun onAttached() {
        super.onAttached()
        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        invalidateVisible()
    }

    override fun onDetached() {
        sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        super.onDetached()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key ?: return
        if (visibleKeysOff?.contains(key) == true || visibleKeysOn?.contains(key) == true) {
            invalidateVisible()
        }
    }
}
