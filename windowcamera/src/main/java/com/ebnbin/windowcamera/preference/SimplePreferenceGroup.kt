package com.ebnbin.windowcamera.preference

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import androidx.preference.PreferenceGroup
import com.ebnbin.eb.sharedpreferences.get
import com.ebnbin.windowcamera.R

/**
 * 空 PreferenceGroup. 用于添加并管理其他 Preference.
 */
open class SimplePreferenceGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : PreferenceGroup(context, attrs, defStyleAttr, defStyleRes), SharedPreferences.OnSharedPreferenceChangeListener {
    init {
        layoutResource = R.layout.simple_preference_group
    }

    /**
     * 当 first set 中的所有 key 对应 value 都为 false 且 second set 中的所有 key 对应 value 都为 true 时 Preference 才可见.
     * 需要在被添加到 PreferenceScreen 之后调用.
     */
    var visibleKeys: Pair<Set<String>?, Set<String>?>? = null
        set(value) {
            if (field == value) return
            field = value
            invalidateVisible()
        }

    private fun invalidateVisible() {
        isVisible = visibleKeys?.first?.all { sharedPreferences?.get(it, false) != true } != false &&
                visibleKeys?.second?.all { sharedPreferences?.get(it, true) != false } != false
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
        key ?: return
        if (visibleKeys?.first?.contains(key) == true || visibleKeys?.second?.contains(key) == true) {
            invalidateVisible()
        }
    }
}
