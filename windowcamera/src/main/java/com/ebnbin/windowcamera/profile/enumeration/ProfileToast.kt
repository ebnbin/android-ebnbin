package com.ebnbin.windowcamera.profile.enumeration

import com.ebnbin.eb.EBApplication
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper

enum class ProfileToast(val entryValue: String, val entry: CharSequence) {
    SYSTEM_ALERT_WINDOW("system_alert_window", EBApplication.instance.resources.getString(R.string.profile_toast_entry_system_alert_window)),
    SYSTEM_ALERT_WINDOW_CENTER("system_alert_window_center",
        EBApplication.instance.resources.getString(R.string.profile_toast_entry_system_alert_window_center)),
    SYSTEM("system", EBApplication.instance.resources.getString(R.string.profile_toast_entry_system)),
    NONE("none", EBApplication.instance.resources.getString(R.string.profile_toast_entry_none));

    companion object {
        fun get(entryValue: String = ProfileHelper.toast.value): ProfileToast {
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
