package com.ebnbin.eb.library

import com.ebnbin.eb.util.DeviceHelper
import com.ebnbin.eb.util.ebApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.greenrobot.eventbus.EventBus

object Libraries {
    val eventBus: EventBus = EventBus.getDefault()

    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(ebApp).apply {
        setUserId(DeviceHelper.DEVICE_ID)
    }
}
