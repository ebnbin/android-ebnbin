package com.ebnbin.windowcamera.main

import android.os.Bundle
import com.ebnbin.eb2.activity.EBActivity

class MainActivity : EBActivity() {
    override fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        extras.putSerializable(KEY_FRAGMENT_CLASS, MainFragment::class.java)
        super.onInitArguments(savedInstanceState, extras)
    }
}
