package com.ebnbin.windowcamera.preference

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.Preference
import com.ebnbin.windowcamera.R

/**
 * 高度 100dp.
 */
open class FooterPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(context, R.attr.preferenceStyle, android.R.attr.preferenceStyle),
    defStyleRes: Int = 0
) : Preference(context, attrs, defStyleAttr, defStyleRes) {
    init {
        layoutResource = R.layout.footer_preference
    }
}
