package com.ebnbin.windowcamera

import com.ebnbin.eb.app.EBApplicationConfig
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.windowcamera.debug.DebugPageFragment

object AppApplicationConfig : EBApplicationConfig() {
    override val debugPageFragmentClass: Class<out BaseDebugPageFragment>? = DebugPageFragment::class.java
}
