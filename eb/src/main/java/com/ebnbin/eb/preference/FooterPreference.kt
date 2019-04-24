package com.ebnbin.eb.preference

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import com.ebnbin.eb.R

/**
 * 高度 100dp.
 */
open class FooterPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.ebFooterPreferenceStyle,
    defStyleRes: Int = 0
) : Preference(context, attrs, defStyleAttr, defStyleRes)
