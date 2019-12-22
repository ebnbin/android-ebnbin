package com.ebnbin.eb.util

import com.ebnbin.eb.BuildConfig

object BuildUtil {
    val isGooglePlayFlavor: Boolean
        get() = BuildConfig.FLAVOR == "google"
}
