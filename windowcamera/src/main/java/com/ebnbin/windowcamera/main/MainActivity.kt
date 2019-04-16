package com.ebnbin.windowcamera.main

import android.os.Bundle
import com.ebnbin.eb.app.EBActivity
import com.ebnbin.eb.update.UpdateFragment

class MainActivity : EBActivity() {
    override fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        super.onInitArguments(savedInstanceState, extras)
        fragmentClass = MainFragment::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            UpdateFragment.start(supportFragmentManager, true)
        }
    }
}
