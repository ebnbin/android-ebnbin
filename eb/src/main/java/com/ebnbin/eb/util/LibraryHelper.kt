package com.ebnbin.eb.util

import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus

object LibraryHelper {
    val eventBus: EventBus = EventBus.getDefault()

    val gson: Gson = Gson().newBuilder().setPrettyPrinting().create()
}
