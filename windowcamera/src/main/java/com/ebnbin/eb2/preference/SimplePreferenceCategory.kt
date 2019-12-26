package com.ebnbin.eb2.preference

import android.content.Context
import androidx.preference.PreferenceCategory

class SimplePreferenceCategory(context: Context) : PreferenceCategory(context, null) {
    init {
        shouldDisableView = true
        isSingleLineTitle = false
    }
}
