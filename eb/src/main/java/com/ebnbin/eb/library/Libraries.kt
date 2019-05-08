package com.ebnbin.eb.library

import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus

object Libraries {
    val eventBus: EventBus = EventBus.getDefault()

    val gson: Gson = Gson().newBuilder().setPrettyPrinting().create()
}
