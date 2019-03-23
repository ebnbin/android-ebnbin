package com.ebnbin.windowcamera.module.main

import android.os.Bundle
import com.ebnbin.eb.app.EBActivity

class MainActivity : EBActivity() {
    override fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        super.onInitArguments(savedInstanceState, extras)
        fragmentClass = MainFragment::class.java
    }
}
