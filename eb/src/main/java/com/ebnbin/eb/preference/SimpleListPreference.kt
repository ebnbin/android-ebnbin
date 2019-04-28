package com.ebnbin.eb.preference

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.ListPreference
import com.ebnbin.eb.R

open class SimpleListPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(context, R.attr.dialogPreferenceStyle,
        android.R.attr.dialogPreferenceStyle),
    defStyleRes: Int = 0
) : ListPreference(context, attrs, defStyleAttr, defStyleRes) {
    init {
        setSummaryProvider { entry }
    }
}
