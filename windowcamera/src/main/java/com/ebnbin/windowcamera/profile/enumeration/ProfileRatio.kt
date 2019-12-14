package com.ebnbin.windowcamera.profile.enumeration

import com.ebnbin.eb2.util.res
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper

enum class ProfileRatio(val entryValue: String, val entry: CharSequence) {
    CAPTURE("capture", res.getString(R.string.profile_ratio_entry_capture)),
    SCREEN("screen", res.getString(R.string.profile_ratio_entry_screen)),
    SQUARE("square", res.getString(R.string.profile_ratio_entry_square));

    companion object {
        fun get(entryValue: String = ProfileHelper.ratio.value): ProfileRatio {
            return values().first { it.entryValue == entryValue }
        }

        fun entryValues(): Array<String> {
            return values().map { it.entryValue }.toTypedArray()
        }

        fun entries(): Array<out CharSequence> {
            return values().map { it.entry }.toTypedArray()
        }
    }
}
