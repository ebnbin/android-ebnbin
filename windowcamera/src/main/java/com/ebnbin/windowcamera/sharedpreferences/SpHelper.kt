package com.ebnbin.windowcamera.sharedpreferences

import com.ebnbin.eb.sharedpreferences.delegate.SharedPreferencesDelegate

object SpHelper {
    var main_page: Int by SharedPreferencesDelegate("main_page", 1)
}
