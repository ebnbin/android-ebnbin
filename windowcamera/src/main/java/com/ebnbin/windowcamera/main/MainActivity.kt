package com.ebnbin.windowcamera.main

import androidx.fragment.app.Fragment
import com.ebnbin.eb.fragment.FragmentActivity

class MainActivity : FragmentActivity() {
    override val fragmentClass: Class<out Fragment>
        get() = MainFragment::class.java
}
