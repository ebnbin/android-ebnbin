package com.ebnbin.eb.app2.library

import com.ebnbin.eb.app2.EBApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object Libraries {
    val firebaseAnalytics: FirebaseAnalytics
        get() = FirebaseAnalytics.getInstance(EBApp.instance)

    val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()
}
