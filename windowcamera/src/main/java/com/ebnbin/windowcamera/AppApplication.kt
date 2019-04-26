package com.ebnbin.windowcamera

import com.ebnbin.eb.app.EBApplication
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.windowcamera.debug.DebugPageFragment

class AppApplication : EBApplication() {
    override val debugPageFragmentClass: Class<out BaseDebugPageFragment>? = DebugPageFragment::class.java
}
