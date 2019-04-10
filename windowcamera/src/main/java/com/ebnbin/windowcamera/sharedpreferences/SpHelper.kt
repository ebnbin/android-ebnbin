package com.ebnbin.windowcamera.sharedpreferences

import com.ebnbin.eb.sharedpreferences.delegate.SharedPreferencesDelegate

object SpHelper {
    var profile: String by SharedPreferencesDelegate("profile", "default")
}
