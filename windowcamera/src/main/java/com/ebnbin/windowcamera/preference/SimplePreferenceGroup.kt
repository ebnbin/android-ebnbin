package com.ebnbin.windowcamera.preference

import android.content.Context
import android.util.AttributeSet
import androidx.preference.PreferenceGroup
import com.ebnbin.windowcamera.R

/**
 * 空 PreferenceGroup. 用于添加并管理其他 Preference.
 */
open class SimplePreferenceGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : PreferenceGroup(context, attrs, defStyleAttr, defStyleRes) {
    init {
        layoutResource = R.layout.simple_preference_group
    }
}
