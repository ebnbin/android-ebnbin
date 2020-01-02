package com.ebnbin.ebapp.library

import com.ebnbin.eb.EBApplication
import com.google.firebase.analytics.FirebaseAnalytics

val firebaseAnalytics: FirebaseAnalytics
    get() = FirebaseAnalytics.getInstance(EBApplication.instance)
