package com.ebnbin.sample

import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.sample.debug.DebugPageFragment

class AppApplication : EBApplication() {
    override val debugPageFragmentClass: Class<out BaseDebugPageFragment>? = DebugPageFragment::class.java
}
