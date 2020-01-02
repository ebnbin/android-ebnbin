package com.ebnbin.eb.app2.library

import com.ebnbin.eb.app2.EBApp
import com.google.firebase.analytics.FirebaseAnalytics

object Libraries {
    val firebaseAnalytics: FirebaseAnalytics
        get() = FirebaseAnalytics.getInstance(EBApp.instance)
}
