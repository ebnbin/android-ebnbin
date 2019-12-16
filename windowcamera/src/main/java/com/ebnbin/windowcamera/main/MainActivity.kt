package com.ebnbin.windowcamera.main

import androidx.fragment.app.Fragment
import com.ebnbin.eb2.activity.EBFragmentActivity

class MainActivity : EBFragmentActivity() {
    override val fragmentClass: Class<out Fragment>
        get() = MainFragment::class.java
}
