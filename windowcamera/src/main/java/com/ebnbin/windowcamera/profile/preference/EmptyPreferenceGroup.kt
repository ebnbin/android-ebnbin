package com.ebnbin.windowcamera.profile.preference

import android.content.Context
import androidx.preference.PreferenceGroup
import com.ebnbin.windowcamera.R

class EmptyPreferenceGroup(context: Context) : PreferenceGroup(context, null) {
    init {
        layoutResource = R.layout.profile_empty_preference_group
    }
}
