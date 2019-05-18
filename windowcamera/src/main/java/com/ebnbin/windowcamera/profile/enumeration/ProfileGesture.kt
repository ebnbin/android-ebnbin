package com.ebnbin.windowcamera.profile.enumeration

import com.ebnbin.eb.util.res
import com.ebnbin.windowcamera.R

enum class ProfileGesture(val entryValue: String, val entry: CharSequence) {
    CAPTURE("capture", res.getString(R.string.profile_gesture_entry_capture)),
    SWITCH_IS_FRONT("switch_is_front", res.getString(R.string.profile_gesture_entry_switch_is_front)),
    SWITCH_IS_PREVIEW("switch_is_preview", res.getString(R.string.profile_gesture_entry_switch_is_preview)),
    SWITCH_IS_VIDEO("switch_is_video", res.getString(R.string.profile_gesture_entry_switch_is_video)),
    RESTART_APP("restart_app", res.getString(R.string.profile_gesture_entry_restart_app)),
    CLOSE_APP("close_app", res.getString(R.string.profile_gesture_entry_close_app)),
    NONE("none", res.getString(R.string.profile_gesture_entry_none));

    companion object {
        fun get(entryValue: String): ProfileGesture {
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
