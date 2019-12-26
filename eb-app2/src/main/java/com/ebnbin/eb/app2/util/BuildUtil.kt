package com.ebnbin.eb.app2.util

import com.ebnbin.eb.BuildConfig

object BuildUtil {
    val isGooglePlayFlavor: Boolean
        get() = BuildConfig.FLAVOR == "google"
}
