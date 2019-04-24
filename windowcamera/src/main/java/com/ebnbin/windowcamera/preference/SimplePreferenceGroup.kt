package com.ebnbin.windowcamera.preference

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import androidx.preference.PreferenceGroup
import androidx.preference.TwoStatePreference
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
     * 当 first set 中的所有 TwoStatePreference 都为 !isChecked 且 second set 中的所有 TwoStatePreference 都为 isChecked 时
     * 当前 Preference 才可见.
     */
    var visibleTwoStatePreferences: Pair<Set<TwoStatePreference>?, Set<TwoStatePreference>?>? = null
        set(value) {
            if (field == value) return
            field = value
            invalidateVisible()
        }

    private fun invalidateVisible() {
        isVisible = visibleTwoStatePreferences?.first?.all { !it.isChecked } != false &&
                visibleTwoStatePreferences?.second?.all { it.isChecked } != false
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
        if (visibleTwoStatePreferences?.first?.any { it.key == key } == true ||
            visibleTwoStatePreferences?.second?.any { it.key == key } == true) {
            invalidateVisible()
        }
    }
}
