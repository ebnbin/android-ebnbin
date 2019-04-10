package com.ebnbin.windowcamera.sharedpreferences

import com.ebnbin.eb.sharedpreferences.delegate.SharedPreferencesDelegate

object SpHelper {
    var profile: String by SharedPreferencesDelegate("profile", "default")

    var main_page: Int by SharedPreferencesDelegate("main_page", 1)
}
