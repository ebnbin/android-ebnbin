package com.ebnbin.eb.library

import org.greenrobot.eventbus.EventBus

val eventBus: EventBus
    get() = EventBus.getDefault()
