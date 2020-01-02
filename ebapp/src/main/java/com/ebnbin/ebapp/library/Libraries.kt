package com.ebnbin.ebapp.library

import com.ebnbin.ebapp.EBAppApplication
import com.google.firebase.analytics.FirebaseAnalytics

val firebaseAnalytics: FirebaseAnalytics
    get() = FirebaseAnalytics.getInstance(EBAppApplication.instance)
