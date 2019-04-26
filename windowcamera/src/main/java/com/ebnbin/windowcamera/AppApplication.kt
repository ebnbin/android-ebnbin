package com.ebnbin.windowcamera

import com.ebnbin.eb.app.EBActivity
import com.ebnbin.eb.app.EBApplication
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.windowcamera.debug.DebugPageFragment
import com.ebnbin.windowcamera.main.MainActivity

class AppApplication : EBApplication() {
    override val mainActivityClass: Class<out EBActivity>? = MainActivity::class.java

    override val debugPageFragmentClass: Class<out BaseDebugPageFragment>? = DebugPageFragment::class.java
}
