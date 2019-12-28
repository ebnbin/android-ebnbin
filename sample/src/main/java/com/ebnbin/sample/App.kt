package com.ebnbin.sample

import com.ebnbin.eb.app2.EBApp
import com.ebnbin.sample.dev.Dev

class App : EBApp() {
    override fun onCreate() {
        super.onCreate()
        Dev.init(this)
    }
}
