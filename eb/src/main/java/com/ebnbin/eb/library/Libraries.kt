package com.ebnbin.eb.library

import com.ebnbin.eb.EBApp
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
