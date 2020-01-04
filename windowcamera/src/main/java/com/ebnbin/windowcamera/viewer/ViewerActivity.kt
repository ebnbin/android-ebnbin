package com.ebnbin.windowcamera.viewer

import androidx.fragment.app.Fragment
import com.ebnbin.eb.fragment.FragmentActivity

class ViewerActivity : FragmentActivity() {
    override val fragmentClass: Class<out Fragment>
        get() = ViewerFragment::class.java
}
